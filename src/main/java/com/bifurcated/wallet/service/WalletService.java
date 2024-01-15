package com.bifurcated.wallet.service;

import com.bifurcated.wallet.data.Wallet;
import com.bifurcated.wallet.data.WalletRepo;
import com.bifurcated.wallet.errors.NotEnoughMoneyError;
import com.bifurcated.wallet.errors.WalletNotFoundError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.UUID;


@Service
public class WalletService {
    private final WalletRepo walletRepo;

    @Autowired
    public WalletService(WalletRepo walletRepo) {
        this.walletRepo = walletRepo;
    }

    @Transactional(
            isolation = Isolation.SERIALIZABLE,
            propagation = Propagation.REQUIRES_NEW)
    @Retryable(retryFor = SQLException.class, maxAttempts = 30, backoff = @Backoff(delay = 500))
    public Wallet addAmount(UUID id, Float amount) {
        var wallet = walletRepo.findById(id).orElseThrow(WalletNotFoundError::new);
        wallet.setAmount(wallet.getAmount() + amount);
        Wallet save = walletRepo.save(wallet);
        return save;
    }

    @Transactional
    public Wallet reduceAmount(UUID id, Float amount) {
        var wallet = walletRepo.findById(id).orElseThrow(WalletNotFoundError::new);
        var reduce = wallet.getAmount() - amount;
        if (reduce < 0) {
            throw new NotEnoughMoneyError(wallet.getAmount(), amount);
        }
        wallet.setAmount(reduce);
        return walletRepo.save(wallet);
    }

    public Float amount(UUID id) {
        return walletRepo.findById(id).orElseThrow(WalletNotFoundError::new).getAmount();
    }
}
