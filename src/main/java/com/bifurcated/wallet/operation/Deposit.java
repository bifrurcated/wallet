package com.bifurcated.wallet.operation;

import com.bifurcated.wallet.data.Wallet;
import com.bifurcated.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Deposit implements OperationType {

    @Autowired
    private WalletService walletService;

    @Override
    public String name() {
        return "deposit";
    }

    @Override
    public Wallet handle(UUID id, Float amount) {
        return walletService.addAmount(id, amount);
    }
}
