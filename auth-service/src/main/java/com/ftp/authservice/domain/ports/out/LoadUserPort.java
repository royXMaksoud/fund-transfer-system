package com.ftp.authservice.domain.ports.out;

import com.ftp.authservice.domain.model.User;

import java.util.Optional;

/**
 * Output port to load a user by username.
 * Implemented by infrastructure (DB, external API…)
 */
public interface LoadUserPort
{
    Optional<User> loadByUsername(String username);
}