package com.ftp.fundtransferservice.shared.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class InsufficientBalanceException extends AppException {
    private static final String CODE = "INSUFFICIENT_BALANCE";

    public InsufficientBalanceException(String message) {
        // Now passing HttpStatus and timestamp
        super(message, CODE, HttpStatus.BAD_REQUEST, LocalDateTime.now());
    }
}
