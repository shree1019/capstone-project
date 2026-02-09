package com.example.moneytransfer.dto;

import com.example.moneytransfer.enums.TransactionStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO used when returning transaction history for an account.
 */
@Value
@Builder
public class TransactionLogResponse {

    UUID id;

    Long fromAccountId;

    Long toAccountId;

    BigDecimal amount;

    TransactionStatus status;

    String failureReason;

    OffsetDateTime createdOn;
}

