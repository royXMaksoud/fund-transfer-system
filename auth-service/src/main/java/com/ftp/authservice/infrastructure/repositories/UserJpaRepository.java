package com.ftp.authservice.infrastructure.repositories;

import com.ftp.authservice.infrastructure.db.entities.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {  // Use UUID for primary key
    Optional<UserJpaEntity> findByUsername(String username);  // Find user by username
    boolean existsByUsername(String username);  // Check if the username exists
}
