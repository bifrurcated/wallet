package com.bifurcated.wallet.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table("wallet")
public class Wallet implements Persistable<UUID> {
    @Id
    private UUID id;
    private Float amount;

    @Transient
    @JsonIgnore
    private boolean newWallet;

    @Override
    @Transient
    @JsonIgnore
    public boolean isNew() {
        return this.newWallet || id == null;
    }

    public Wallet setAsNew() {
        this.newWallet = true;
        return this;
    }
}
