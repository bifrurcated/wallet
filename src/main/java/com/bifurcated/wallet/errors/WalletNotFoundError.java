package com.bifurcated.wallet.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WalletNotFoundError extends ResponseStatusException {
    public WalletNotFoundError() {
        super(HttpStatus.NOT_FOUND, "wallet not found");
    }
}
