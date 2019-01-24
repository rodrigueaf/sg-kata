package com.claude.sgkata.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException() {
        super("Insufficient balance to make this withdraw");
    }
}
