package com.example.moneytransfer.dto;

import com.example.moneytransfer.enums.AccountStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Representation of an account exposed via the API.
 */
@Value
@Builder
public class AccountResponse {

    Long id;

    String holderName;

    BigDecimal balance;

    AccountStatus status;

    OffsetDateTime lastUpdated;
}

