package com.bifurcated.wallet.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnsupportedOperationTypeError extends ResponseStatusException {
    public UnsupportedOperationTypeError() {
        super(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "operation type is not supported");
    }
}
