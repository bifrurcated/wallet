package com.bifurcated.wallet.service;

import com.bifurcated.wallet.data.Wallet;
import com.bifurcated.wallet.repository.WalletRepo;
import com.bifurcated.wallet.errors.NotEnoughMoneyError;
import com.bifurcated.wallet.errors.WalletNotFoundError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
public class WalletService {
    private final WalletRepo walletRepo;

    @Autowired
    public WalletService(WalletRepo walletRepo) {
        this.walletRepo = walletRepo;
    }

    @Transactional
    public Wallet addAmount(UUID id, Float amount) {
        Wallet wallet = walletRepo.findByIdForUpdate(id).orElseThrow(WalletNotFoundError::new);
        wallet.setAmount(wallet.getAmount() + amount);
        return walletRepo.save(wallet);
    }

    public Wallet addAmountOneOperation(UUID id, Float amount) {
        return walletRepo.updateAmount(id, amount).orElseThrow(WalletNotFoundError::new);
    }

    @Transactional
    public Wallet reduceAmount(UUID id, Float amount) {
        var wallet = walletRepo.findByIdForUpdate(id).orElseThrow(WalletNotFoundError::new);
        var reduce = wallet.getAmount() - amount;
        if (reduce < 0) {
            throw new NotEnoughMoneyError(wallet.getAmount(), amount);
        }
        wallet.setAmount(reduce);
        return walletRepo.save(wallet);
    }

    public Wallet reduceAmountUpdateReturning(UUID id, Float amount) {
        var wallet = walletRepo.findById(id).orElseThrow(WalletNotFoundError::new);
        var reduce = wallet.getAmount() - amount;
        if (reduce < 0) {
            throw new NotEnoughMoneyError(wallet.getAmount(), amount);
        }
        return walletRepo.updateAmountReduceReturning(id, amount);
    }

    public Wallet reduceAmountUpdateFind(UUID id, Float amount) {
        var wallet = walletRepo.findById(id).orElseThrow(WalletNotFoundError::new);
        var reduce = wallet.getAmount() - amount;
        if (reduce < 0) {
            throw new NotEnoughMoneyError(wallet.getAmount(), amount);
        }
        walletRepo.updateAmountReduce(id, amount);
        return walletRepo.findById(id).orElseThrow(WalletNotFoundError::new);
    }

    public Float amount(UUID id) {
        return walletRepo.findById(id).orElseThrow(WalletNotFoundError::new).getAmount();
    }
}
