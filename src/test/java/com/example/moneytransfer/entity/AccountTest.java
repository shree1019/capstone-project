package com.example.moneytransfer.entity;

import com.example.moneytransfer.enums.AccountStatus;
import com.example.moneytransfer.exception.InsufficientBalanceException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {

    @Test
    void debitShouldDecreaseBalanceWhenSufficient() {
        Account account = Account.builder()
                .id(1L)
                .holderName("Test User")
                .balance(new BigDecimal("100.00"))
                .status(AccountStatus.ACTIVE)
                .build();

        account.debit(new BigDecimal("40.00"));

        assertEquals(new BigDecimal("60.00"), account.getBalance());
    }

    @Test
    void debitShouldThrowWhenInsufficientBalance() {
        Account account = Account.builder()
                .id(1L)
                .holderName("Test User")
                .balance(new BigDecimal("10.00"))
                .status(AccountStatus.ACTIVE)
                .build();

        assertThrows(InsufficientBalanceException.class,
                () -> account.debit(new BigDecimal("20.00")));
    }

    @Test
    void creditShouldIncreaseBalance() {
        Account account = Account.builder()
                .id(1L)
                .holderName("Test User")
                .balance(new BigDecimal("50.00"))
                .status(AccountStatus.ACTIVE)
                .build();

        account.credit(new BigDecimal("25.00"));

        assertEquals(new BigDecimal("75.00"), account.getBalance());
    }
}

