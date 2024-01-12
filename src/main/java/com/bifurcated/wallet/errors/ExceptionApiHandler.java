package com.bifurcated.wallet.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(UnsupportedOperationTypeError.class)
    public ResponseEntity<String> unsupportedOperationType(UnsupportedOperationTypeError exception) {
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(exception.getMessage());
    }

    @ExceptionHandler(WalletNotFoundError.class)
    public ResponseEntity<String> walletNotFound(WalletNotFoundError exception) {
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(exception.getMessage());
    }

    @ExceptionHandler(NotEnoughMoneyError.class)
    public ResponseEntity<String> walletNotFound(NotEnoughMoneyError exception) {
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(exception.getMessage());
    }
}
