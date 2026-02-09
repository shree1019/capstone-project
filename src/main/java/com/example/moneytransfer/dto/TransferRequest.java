package com.example.moneytransfer.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Request payload for initiating a money transfer between two accounts.
 */
@Data
@Builder
public class TransferRequest {

    @NotNull(message = "From account id is required")
    private Long fromAccountId;

    @NotNull(message = "To account id is required")
    private Long toAccountId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be at least 0.01")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    /**
     * Client-generated unique idempotency key to prevent duplicate transfers.
     */
    @NotBlank(message = "Idempotency key is required")
    private String idempotencyKey;
}

