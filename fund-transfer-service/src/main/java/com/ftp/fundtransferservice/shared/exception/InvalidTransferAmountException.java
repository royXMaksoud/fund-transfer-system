package com.ftp.fundtransferservice.shared.exception;

import org.springframework.http.HttpStatus;

public class InvalidTransferAmountException extends AppBaseException {

    /**
     * Constructor to create an InvalidTransferAmountException
     *
     * @param message The custom error message.
     */
    public InvalidTransferAmountException(String message) {
        super(message, "INVALID_TRANSFER_AMOUNT", HttpStatus.BAD_REQUEST);
    }
}
