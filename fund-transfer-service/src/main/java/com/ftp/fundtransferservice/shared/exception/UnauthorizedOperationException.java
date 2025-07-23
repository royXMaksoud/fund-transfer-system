package com.ftp.fundtransferservice.shared.exception;

import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

public class UnauthorizedOperationException extends AppException {
    // Constructor
    public UnauthorizedOperationException(String message) {
        super(message, "UNAUTHORIZED_OPERATION", HttpStatus.FORBIDDEN, LocalDateTime.now());
    }
}
