package com.bifurcated.wallet.service;

import com.bifurcated.wallet.data.WalletEntity;
import com.bifurcated.wallet.errors.WalletNotFoundError;
import com.bifurcated.wallet.repository.WalletEntityRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class WalletEntityService {

    private final WalletEntityRepository repository;
    private final EntityManager entityManager;

    @Autowired
    public WalletEntityService(WalletEntityRepository walletEntityRepository, EntityManager entityManager) {
        this.repository = walletEntityRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public WalletEntity addAmount(UUID id, Float amount) {
        var entity = entityManager.find(WalletEntity.class, id, LockModeType.PESSIMISTIC_WRITE);
        var wallet = Optional.ofNullable(entity).orElseThrow(WalletNotFoundError::new);
        wallet.setAmount(wallet.getAmount() + amount);
        return repository.save(wallet);
    }

    public WalletEntity addAmountUsingUpdate(UUID id, Float amount) {
        repository.updateAddAmountById(id, amount);
        return repository.findById(id).orElseThrow(WalletNotFoundError::new);
    }

    public Float amount(UUID id) {
        return repository.findById(id).orElseThrow(WalletNotFoundError::new).getAmount();
    }
}
