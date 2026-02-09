package com.example.moneytransfer.dto;

import com.example.moneytransfer.enums.TransactionStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Response payload returned after processing a transfer request.
 */
@Value
@Builder
public class TransferResponse {

    UUID transactionId;

    Long fromAccountId;

    Long toAccountId;

    BigDecimal amount;

    TransactionStatus status;

    String message;

    OffsetDateTime createdOn;
}

