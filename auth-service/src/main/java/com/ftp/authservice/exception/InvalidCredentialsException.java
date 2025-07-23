package com.ftp.authservice.exception;

/**
 * Custom exception for handling invalid credentials during authentication.
 * This exception is thrown when the login credentials (username/password) are incorrect.
 */
public class InvalidCredentialsException extends RuntimeException {

    /**
     * Constructor to create an instance of InvalidCredentialsException.
     * @param message The error message that will be passed when this exception is thrown.
     */
    public InvalidCredentialsException(String message) {
        super(message);  // Pass the error message to the parent class (RuntimeException)
    }
}
