package com.bifurcated.wallet.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Setter
@Getter
@ToString
@Entity(name = "wallet")
public class WalletEntity {

    @Id
    @GeneratedValue(generator="UseIdOrGenerate")
    @GenericGenerator(name="UseIdOrGenerate", type = UseIdOrGenerate.class)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    private Float amount;
}
