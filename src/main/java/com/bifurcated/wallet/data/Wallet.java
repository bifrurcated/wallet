package com.bifurcated.wallet.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
@Table("wallet")
public class Wallet {
    @Id
    private UUID id;

    @Setter
    private Float amount;
}
