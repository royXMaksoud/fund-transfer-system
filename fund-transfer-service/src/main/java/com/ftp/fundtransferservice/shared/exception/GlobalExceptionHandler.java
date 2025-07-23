package com.ftp.fundtransferservice.web.exception;

import com.ftp.fundtransferservice.shared.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle AppException (e.g., account not found, unauthorized operation)
    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, Object>> handleAppException(AppException ex) {
        Map<String, Object> response = buildBaseError(ex.getMessage(), ex.getErrorCode());
        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    // Handle InsufficientBalanceException (inherits from AppException)
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientBalance(InsufficientBalanceException ex) {
        Map<String, Object> response = buildBaseError(ex.getMessage(), ex.getErrorCode());
        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    // Handle InvalidTransferAmountException (inherits from AppBaseException)
    @ExceptionHandler(InvalidTransferAmountException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidAmount(InvalidTransferAmountException ex) {
        Map<String, Object> response = buildBaseError(ex.getMessage(), ex.getErrorCode());
        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    // Handle generic fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAnyException(Exception ex) {
        Map<String, Object> response = buildBaseError("Internal server error", "INTERNAL_SERVER_ERROR");
        response.put("details", ex.getMessage());
        return ResponseEntity.status(500).body(response);
    }

    // Shared error response formatter
    private Map<String, Object> buildBaseError(String message, String code) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", Instant.now());
        response.put("status", "error");
        response.put("message", message);
        response.put("code", code);
        return response;
    }
}
