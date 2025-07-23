package com.ftp.authservice.infrastructure.security;

import com.ftp.authservice.infrastructure.db.entities.RefreshTokenEntity;
import com.ftp.authservice.infrastructure.repositories.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public boolean isExpired(RefreshTokenEntity token) {
        return token.isExpired();
    }

    public RefreshTokenEntity createRefreshToken(String username, String role) {
        // Generate a refresh token and set an expiry date
        RefreshTokenEntity token = new RefreshTokenEntity();
        token.setUsername(username);
        token.setToken(generateToken());
        token.setExpiryDate(Instant.now().plusSeconds(30 * 24 * 60 * 60)); // 30 days
        return refreshTokenRepository.save(token);
    }

    private String generateToken() {
        // Generate a random token
        return UUID.randomUUID().toString();
    }
}
