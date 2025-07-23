package com.ftp.authservice.exception;

public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Constructor to create an instance of UserAlreadyExistsException.
     * @param message The error message that will be passed when this exception is thrown.
     */
    public UserAlreadyExistsException(String message) {
        super(message);  // Pass the error message to the parent class (RuntimeException)
    }
}
