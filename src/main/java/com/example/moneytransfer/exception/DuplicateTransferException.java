package com.example.moneytransfer.exception;

/**
 * Thrown when a transfer with the same idempotency key has already been processed.
 */
public class DuplicateTransferException extends RuntimeException {

    public DuplicateTransferException(String idempotencyKey) {
        super("Transfer with idempotency key '" + idempotencyKey + "' has already been processed");
    }
}

