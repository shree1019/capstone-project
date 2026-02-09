package com.example.moneytransfer.exception;

/**
 * Thrown when an account does not have enough balance to complete a debit.
 */
public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String message) {
        super(message);
    }
}

