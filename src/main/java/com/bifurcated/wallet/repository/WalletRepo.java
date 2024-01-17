package com.bifurcated.wallet.repository;

import com.bifurcated.wallet.data.Wallet;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.relational.core.sql.LockMode;
import org.springframework.data.relational.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

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
    Wallet updateAmountReduceReturning(UUID id, Float amount);

    @Query("""
         UPDATE wallet
         SET amount = (amount - :amount)
         WHERE id = :id
    """)
    @Modifying
    void updateAmountReduce(UUID id, Float amount);

    @Query("SELECT w.id, w.amount FROM wallet w WHERE id = :id FOR UPDATE")
    Optional<Wallet> findByIdForUpdate(@Param("id") UUID id);
}
