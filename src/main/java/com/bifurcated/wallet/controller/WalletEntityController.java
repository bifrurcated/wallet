package com.bifurcated.wallet.controller;

import com.bifurcated.wallet.operation.OperationType;
import com.bifurcated.wallet.service.WalletEntityService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v2")
public class WalletEntityController {

    private final WalletEntityService walletService;
    private final Map<String, OperationType> operations;


    @Autowired
    public WalletEntityController(WalletEntityService walletEntityService, Map<String, OperationType> operationTypeMap) {
        this.walletService = walletEntityService;
        this.operations = operationTypeMap;
    }

    public record WalletResponse(UUID id, Float amount){}
    public record WalletRequest(
            @JsonProperty("valletId") UUID walletId,
            Float amount
    ){}
    @PostMapping("/wallet/add")
    public WalletResponse walletAdd(@RequestBody WalletRequest request) {
        var wallet = walletService.addAmount(request.walletId(), request.amount());
        return new WalletResponse(wallet.getId(), wallet.getAmount());
    }

    @PostMapping("/wallet/reduce")
    public WalletResponse walletReduce(@RequestBody WalletRequest request) {
        var wallet = walletService.reduceAmount(request.walletId(), request.amount());
        return new WalletResponse(wallet.getId(), wallet.getAmount());
    }

    public record BalanceResponse(Float amount){}
    @GetMapping("/wallets/{WALLET_UUID}")
    public BalanceResponse balance(@PathVariable(value = "WALLET_UUID") UUID id) {
        Float amount = walletService.amount(id);
        return new BalanceResponse(amount);
    }
}
