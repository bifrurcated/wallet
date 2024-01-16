package com.bifurcated.wallet.service;

import com.bifurcated.wallet.data.Wallet;
import com.bifurcated.wallet.data.WalletRepo;
import com.bifurcated.wallet.errors.NotEnoughMoneyError;
import com.bifurcated.wallet.errors.WalletNotFoundError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class WalletService {
    private final WalletRepo walletRepo;

    @Autowired
    public WalletService(WalletRepo walletRepo) {
        this.walletRepo = walletRepo;
    }

    public Wallet addAmount(UUID id, Float amount) {
        return walletRepo.updateAmount(id, amount).orElseThrow(WalletNotFoundError::new);
    }

    public Wallet reduceAmount(UUID id, Float amount) {
        var wallet = walletRepo.findById(id).orElseThrow(WalletNotFoundError::new);
        var reduce = wallet.getAmount() - amount;
        if (reduce < 0) {
            throw new NotEnoughMoneyError(wallet.getAmount(), amount);
        }
        return walletRepo.updateAmountReduce(id, amount);
    }

    public Float amount(UUID id) {
        return walletRepo.findById(id).orElseThrow(WalletNotFoundError::new).getAmount();
    }
}
