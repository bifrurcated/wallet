package com.bifurcated.wallet.data;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface WalletRepo extends CrudRepository<Wallet, UUID> {
}
