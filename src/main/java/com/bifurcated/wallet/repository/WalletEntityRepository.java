package com.bifurcated.wallet.repository;

import com.bifurcated.wallet.data.WalletEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface WalletEntityRepository extends JpaRepository<WalletEntity, UUID>, JpaSpecificationExecutor<WalletEntity> {
    @Transactional
    @Modifying
    @Query("update wallet w set w.amount = ?1 where w.id = ?2")
    int updateAmountById(Float amount, UUID id);

    @Transactional
    @Modifying
    @Query("update wallet w set w.amount = w.amount + ?2 where w.id = ?1")
    void updateAddAmountById(UUID id, Float amount);

    @Query("SELECT w FROM wallet w WHERE w.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<WalletEntity> findByIdForUpdate(UUID id);

}