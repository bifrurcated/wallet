package com.bifurcated.wallet.data;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepo extends CrudRepository<Wallet, UUID> {

    @Query("""
         UPDATE wallet
         SET amount = (amount + :amount)
         WHERE id = :id
         RETURNING id, amount
    """)
    Optional<Wallet> updateAmount(UUID id, Float amount);

    @Query("""
         UPDATE wallet
         SET amount = (amount - :amount)
         WHERE id = :id
         RETURNING id, amount
    """)
    Wallet updateAmountReduce(UUID id, Float amount);

}
