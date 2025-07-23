package com.ftp.fundtransferservice.shared.exception;

import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

public class AppException extends RuntimeException {

    private final String errorCode;
    private final LocalDateTime timestamp;
    private final HttpStatus httpStatus;

    public AppException(String message, String errorCode, HttpStatus httpStatus, LocalDateTime timestamp) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }

    public AppException(String message, String errorCode, Throwable cause, HttpStatus httpStatus, LocalDateTime timestamp) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
