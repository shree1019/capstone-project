package com.example.moneytransfer.exception;

/**
 * Thrown when an account cannot be found for the given identifier.
 */
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(Long accountId) {
        super("Account not found for id: " + accountId);
    }
}

