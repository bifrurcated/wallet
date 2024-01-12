package com.bifurcated.wallet.controller;

import com.bifurcated.wallet.data.Wallet;
import com.bifurcated.wallet.errors.UnsupportedOperationTypeError;
import com.bifurcated.wallet.service.WalletService;
import com.bifurcated.wallet.operation.OperationType;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/wallet")
public class WalletController {

    private final WalletService walletService;
    private final Map<String, OperationType> operations;


    @Autowired
    public WalletController(WalletService walletService, Map<String, OperationType> operationTypeMap) {
        this.walletService = walletService;
        this.operations = operationTypeMap;
    }

    public record WalletResponse(UUID id, Float amount){}
    public record WalletRequest(
            @JsonProperty("valletId") UUID walletId,
            String operationType,
            Float amount
    ){}
    @PostMapping
    public WalletResponse wallet(@RequestBody WalletRequest request) {
        String operationType = request.operationType().toLowerCase();
        if (operations.containsKey(operationType)) {
            Wallet wallet = operations.get(operationType).handle(request.walletId(), request.amount());
            return new WalletResponse(wallet.getId(), wallet.getAmount());
        }
        throw new UnsupportedOperationTypeError();
    }
}
