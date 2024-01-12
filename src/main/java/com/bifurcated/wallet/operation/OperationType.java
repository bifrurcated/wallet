package com.bifurcated.wallet.operation;

import com.bifurcated.wallet.data.Wallet;

import java.util.UUID;

public interface OperationType {
    String name();

    Wallet handle(UUID id, Float amount);
}
