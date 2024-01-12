package com.bifurcated.wallet.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotEnoughMoneyError extends ResponseStatusException {
    public NotEnoughMoneyError(Float walletAmount, Float amount) {
        super(HttpStatus.PAYMENT_REQUIRED, "not enough money (%s) on the wallet, required amount: %s".formatted(walletAmount, amount));
    }
}
