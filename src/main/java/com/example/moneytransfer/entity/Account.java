package com.example.moneytransfer.entity;

import com.example.moneytransfer.enums.AccountStatus;
import com.example.moneytransfer.exception.InsufficientBalanceException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "holder_name", nullable = false, length = 100)
    private String holderName;

    @Column(name = "balance", nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AccountStatus status;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "last_updated", nullable = false)
    private OffsetDateTime lastUpdated;

    public boolean isActive() {
        return AccountStatus.ACTIVE.equals(this.status);
    }

    public void debit(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Debit amount must be positive");
        }
        if (this.balance == null) {
            throw new IllegalStateException("Account balance is not initialized");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for debit operation");
        }
        this.balance = this.balance.subtract(amount);
        this.lastUpdated = OffsetDateTime.now();
    }

    public void credit(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Credit amount must be positive");
        }
        this.balance = this.balance.add(amount);
        this.lastUpdated = OffsetDateTime.now();
    }
}

