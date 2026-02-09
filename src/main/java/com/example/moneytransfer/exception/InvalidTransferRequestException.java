package com.example.moneytransfer.exception;

/**
 * Thrown when a transfer request violates basic invariants
 * such as attempting to transfer between the same account.
 */
public class InvalidTransferRequestException extends RuntimeException {

    public InvalidTransferRequestException(String message) {
        super(message);
    }
}

