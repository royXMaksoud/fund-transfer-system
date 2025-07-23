package com.ftp.authservice.domain.ports.in;

import com.ftp.authservice.domain.model.User;

public interface  LoginUseCase {
    /**
     * Authenticates a user by username and password.
     *
     * @param username the user's username
     * @param password the user's password
     * @return the authenticated user or throws exception
     */
    User login(String username, String password);

}
