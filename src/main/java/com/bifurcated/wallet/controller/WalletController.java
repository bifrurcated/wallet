package com.bifurcated.wallet.controller;

import com.bifurcated.wallet.errors.UnsupportedOperationTypeError;
import com.bifurcated.wallet.operation.OperationType;
import com.bifurcated.wallet.service.WalletService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
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
    @PostMapping("/wallet")
    public WalletResponse wallet(@RequestBody WalletRequest request) {
        var operationType = request.operationType().toLowerCase();
        var wallet = Optional.ofNullable(operations.get(operationType))
                .orElseThrow(UnsupportedOperationTypeError::new)
                .handle(request.walletId(), request.amount());

        return new WalletResponse(wallet.getId(), wallet.getAmount());
    }

    public record BalanceResponse(Float amount){}
    @GetMapping("/wallets/{WALLET_UUID}")
    public BalanceResponse balance(@PathVariable(value = "WALLET_UUID") UUID id) {
        Float amount = walletService.amount(id);
        return new BalanceResponse(amount);
    }
}
