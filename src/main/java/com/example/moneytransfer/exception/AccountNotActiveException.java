package com.example.moneytransfer.exception;

/**
 * Thrown when an operation is attempted on an account that is not ACTIVE.
 */
public class AccountNotActiveException extends RuntimeException {

    public AccountNotActiveException(Long accountId) {
        super("Account with id " + accountId + " is not active");
    }
}

