package com.ftp.fundtransferservice.shared.exception;

import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

public class AppBaseException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final LocalDateTime timestamp;

    /**
     * Constructor to store message, errorCode, HTTP status, and timestamp
     *
     * @param message The custom error message.
     * @param errorCode The error code associated with this exception.
     * @param httpStatus The HTTP status code to be returned.
     */
    public AppBaseException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor to store message, errorCode, cause, HTTP status, and timestamp
     *
     * @param message The custom error message.
     * @param errorCode The error code associated with this exception.
     * @param cause The cause of the exception.
     * @param httpStatus The HTTP status code to be returned.
     */
    public AppBaseException(String message, String errorCode, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.timestamp = LocalDateTime.now();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
