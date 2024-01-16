package com.bifurcated.wallet.controller;

import com.bifurcated.wallet.data.Wallet;
import com.bifurcated.wallet.data.WalletRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WalletControllerTest {
    private static Logger logger = LoggerFactory.getLogger(WalletControllerTest.class);
    private static final String END_POINT_PATH = "/api/v1";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WalletRepo repository;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
        Wallet wallet1 = new Wallet();
        wallet1.setId(UUID.fromString("b3919077-79e6-4570-bfe0-980ef18f3731"));
        wallet1.setAmount(2000F);
        if (repository.findById(wallet1.getId()).isEmpty()) {
            repository.save(wallet1.setAsNew());
        }
        Wallet wallet2 = new Wallet();
        wallet2.setId(UUID.fromString("bc10fad7-94f1-4047-be4e-311247eed5fb"));
        wallet2.setAmount(500F);
        if (repository.findById(wallet2.getId()).isEmpty()) {
            repository.save(wallet2.setAsNew());
        }
        Wallet wallet3 = new Wallet();
        wallet3.setId(UUID.fromString("9ebef4de-68e3-43ad-a812-42193919ff02"));
        wallet3.setAmount(302000F);
        if (repository.findById(wallet3.getId()).isEmpty()) {
            repository.save(wallet3.setAsNew());
        }
    }
    @AfterEach
    public void clear() {
        repository.deleteAll();
    }

    @Test
    public void testWalletDeposit() throws Exception {
        record WalletRequest(UUID valletId, String operationType, Float amount){}
        record WalletResponse(UUID id, Float amount) {}
        UUID id = UUID.fromString("b3919077-79e6-4570-bfe0-980ef18f3731");
        WalletRequest walletRequest = new WalletRequest(
                id, "DEPOSIT", 1000F);
        WalletResponse walletResponse = new WalletResponse(
                id, 3000F
        );
        String requestBody = objectMapper.writeValueAsString(walletRequest);
        String response = objectMapper.writeValueAsString(walletResponse);
        this.mockMvc.perform(post(END_POINT_PATH+"/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }
    @Test
    public void testWalletDepositNotFound() throws Exception {
        record WalletRequest(UUID valletId, String operationType, Float amount){}
        WalletRequest walletRequest = new WalletRequest(
                UUID.randomUUID(), "DEPOSIT", 1000F);
        String requestBody = objectMapper.writeValueAsString(walletRequest);
        this.mockMvc.perform(post(END_POINT_PATH+"/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testWalletWithdraw() throws Exception {
        record WalletRequest(UUID valletId, String operationType, Float amount){}
        record WalletResponse(UUID id, Float amount) {}
        UUID id = UUID.fromString("b3919077-79e6-4570-bfe0-980ef18f3731");
        WalletRequest walletRequest = new WalletRequest(
                id, "WITHDRAW", 1000F);
        WalletResponse walletResponse = new WalletResponse(
                id, 1000F
        );
        String requestBody = objectMapper.writeValueAsString(walletRequest);
        String response = objectMapper.writeValueAsString(walletResponse);
        this.mockMvc.perform(post(END_POINT_PATH+"/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    public void testWalletWithdrawNotEnoughMoney() throws Exception {
        record WalletRequest(UUID valletId, String operationType, Float amount){}
        UUID id = UUID.fromString("bc10fad7-94f1-4047-be4e-311247eed5fb");
        WalletRequest walletRequest = new WalletRequest(
                id, "WITHDRAW", 1000F);

        String requestBody = objectMapper.writeValueAsString(walletRequest);
        this.mockMvc.perform(post(END_POINT_PATH+"/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isPaymentRequired());
    }

    @Test
    public void testWalletWithdrawUnsupportedOperationType() throws Exception {
        record WalletRequest(UUID valletId, String operationType, Float amount){}
        UUID id = UUID.fromString("bc10fad7-94f1-4047-be4e-311247eed5fb");
        WalletRequest walletRequest = new WalletRequest(id, "WWWWWWW", 1000F);
        String requestBody = objectMapper.writeValueAsString(walletRequest);
        this.mockMvc.perform(post(END_POINT_PATH+"/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void testWalletGetAmount() throws Exception {
        record BalanceResponse(Float amount){}
        BalanceResponse balanceResponse = new BalanceResponse(2000F);
        String response = objectMapper.writeValueAsString(balanceResponse);
        String id = "b3919077-79e6-4570-bfe0-980ef18f3731";
        this.mockMvc.perform(get(END_POINT_PATH+"/wallets/"+id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    public void testWalletGetAmountNotFound() throws Exception {
        String id = "b3919077-79e6-4570-bfe0-980ef18f3700";
        this.mockMvc.perform(get(END_POINT_PATH+"/wallets/"+id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testWalletDepositDDOSAttack() throws Exception {
        record WalletRequest(UUID valletId, String operationType, Float amount){}
        record BalanceResponse(Float amount){}
        UUID id = UUID.fromString("b3919077-79e6-4570-bfe0-980ef18f3731");
        WalletRequest walletRequest = new WalletRequest(
                id, "DEPOSIT", 1000F);

        String requestBody = objectMapper.writeValueAsString(walletRequest);

        ExecutorService executorService = Executors.newFixedThreadPool(300);
        List<Future<ResultActions>> futures = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            Callable<ResultActions> callable = () -> {
                ResultActions perform = this.mockMvc.perform(post(END_POINT_PATH + "/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));
                return perform.andExpect(status().isOk());
            };

            Future<ResultActions> future = executorService.submit(callable);
            futures.add(future);
        }
        for (Future<ResultActions> future : futures) {
            future.get();
        }

        BalanceResponse balanceResponse = new BalanceResponse(302000F);
        String response = objectMapper.writeValueAsString(balanceResponse);
        this.mockMvc.perform(get(END_POINT_PATH+"/wallets/"+id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    public void testWalletWithdrawDDOSAttack() throws Exception {
        record WalletRequest(UUID valletId, String operationType, Float amount){}
        record BalanceResponse(Float amount){}
        UUID id = UUID.fromString("9ebef4de-68e3-43ad-a812-42193919ff02");
        WalletRequest walletRequest = new WalletRequest(
                id, "WITHDRAW", 1000F);

        String requestBody = objectMapper.writeValueAsString(walletRequest);

        ExecutorService executorService = Executors.newFixedThreadPool(300);
        List<Future<ResultActions>> futures = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            Callable<ResultActions> callable = () -> {
                ResultActions perform = this.mockMvc.perform(post(END_POINT_PATH + "/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));
                return perform.andExpect(status().isOk());
            };

            Future<ResultActions> future = executorService.submit(callable);
            futures.add(future);
        }
        for (Future<ResultActions> future : futures) {
            future.get();
        }

        BalanceResponse balanceResponse = new BalanceResponse(2000F);
        String response = objectMapper.writeValueAsString(balanceResponse);
        this.mockMvc.perform(get(END_POINT_PATH+"/wallets/"+id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    public void testWalletDepositAndWithdrawConcurrency() throws Exception {
        record WalletRequest(UUID valletId, String operationType, Float amount){}
        record BalanceResponse(Float amount){}
        UUID id = UUID.fromString("9ebef4de-68e3-43ad-a812-42193919ff02");
        WalletRequest walletDepositRequest = new WalletRequest(id, "DEPOSIT", 500F);
        WalletRequest walletWithdrawRequest = new WalletRequest(id, "WITHDRAW", 1000F);

        String requestDepositBody = objectMapper.writeValueAsString(walletDepositRequest);
        String requestWithdrawBody = objectMapper.writeValueAsString(walletWithdrawRequest);

        ExecutorService executorService = Executors.newFixedThreadPool(300);
        List<Future<ResultActions>> futures = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            Callable<ResultActions> callable;
            if (i%2 == 0) {
                callable = () -> {
                    ResultActions perform = this.mockMvc.perform(post(END_POINT_PATH + "/wallet")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestDepositBody));
                    return perform.andExpect(status().isOk());
                };
            } else {
                callable = () -> {
                    ResultActions perform = this.mockMvc.perform(post(END_POINT_PATH + "/wallet")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestWithdrawBody));
                    return perform.andExpect(status().isOk());
                };
            }

            Future<ResultActions> future = executorService.submit(callable);
            futures.add(future);
        }
        for (Future<ResultActions> future : futures) {
            future.get();
        }

        BalanceResponse balanceResponse = new BalanceResponse(227000F);
        String response = objectMapper.writeValueAsString(balanceResponse);
        this.mockMvc.perform(get(END_POINT_PATH+"/wallets/"+id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }
}