package com.ftp.authservice.application.service;

import com.ftp.authservice.domain.model.User;
import com.ftp.authservice.domain.ports.in.LoginUseCase;
import com.ftp.authservice.domain.ports.out.LoadUserPort;
import com.ftp.authservice.infrastructure.db.entities.FailedLoginAttempt;
import com.ftp.authservice.infrastructure.repositories.FailedLoginAttemptRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginUseCase {

    private final LoadUserPort loadUserPort;
    private final PasswordEncoder passwordEncoder;
    private final FailedLoginAttemptRepository failedLoginAttemptRepository;

    public LoginServiceImpl(LoadUserPort loadUserPort, PasswordEncoder passwordEncoder, FailedLoginAttemptRepository failedLoginAttemptRepository) {
        this.loadUserPort = loadUserPort;
        this.passwordEncoder = passwordEncoder;
        this.failedLoginAttemptRepository = failedLoginAttemptRepository;
    }

    @Override
    public User login(String username, String password) {
        // Check if the account is locked due to too many failed login attempts
        FailedLoginAttempt failedAttempt = failedLoginAttemptRepository.findByUsername(username).orElse(new FailedLoginAttempt());
        if (failedAttempt.isLocked()) {
            throw new RuntimeException("Account is locked due to too many failed login attempts.");
        }

        // Load user and verify password
        User user = loadUserPort.loadByUsername(username)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElseThrow(() -> {
                    // Increment failed attempts if login fails
                    failedAttempt.incrementAttempts();
                    failedLoginAttemptRepository.save(failedAttempt);
                    throw new RuntimeException("Invalid username or password");
                });

        // Reset failed attempts after successful login
        failedLoginAttemptRepository.delete(failedAttempt);
        return user;
    }
}
