package com.bifurcated.wallet.repository;

import com.bifurcated.wallet.data.WalletEntity;
import jakarta.annotation.Nonnull;
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
}