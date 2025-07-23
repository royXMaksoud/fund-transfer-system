package com.ftp.authservice.application.service;

import com.ftp.authservice.domain.model.User;
import com.ftp.authservice.domain.ports.in.RegisterUserUseCase;
import com.ftp.authservice.domain.ports.out.SaveUserPort;
import com.ftp.authservice.exception.UserAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RegisterServiceImpl implements RegisterUserUseCase {

    private final SaveUserPort saveUserPort;
    private final PasswordEncoder passwordEncoder;

    // Logger initialization
    private static final Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);

    public RegisterServiceImpl(SaveUserPort saveUserPort, PasswordEncoder passwordEncoder) {
        this.saveUserPort = saveUserPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        // Log the attempt to register the user
        logger.info("Attempting to register user with username: {}", user.getUsername());

        // Check if the username already exists
        if (saveUserPort.existsByUsername(user.getUsername())) {
            logger.error("Username already exists: {}", user.getUsername());
            throw new UserAlreadyExistsException("Username already exists");
        }

        // Encrypt the password before saving it
        String encryptedPassword = passwordEncoder.encode(user.getPassword());

        // Create a new User object with encrypted password
        User userToSave = new User(
                user.getId(),
                user.getUsername(),
                encryptedPassword,  // Set the encrypted password
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );

        // Log the successful registration
        logger.info("User registered successfully with username: {}", user.getUsername());

        // Save the user with the encrypted password
        return saveUserPort.save(userToSave);
    }
}
