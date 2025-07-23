package com.ftp.authservice.infrastructure.repositories;

import com.ftp.authservice.infrastructure.db.entities.FailedLoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FailedLoginAttemptRepository extends JpaRepository<FailedLoginAttempt, Long> {
    Optional<FailedLoginAttempt> findByUsername(String username);
}
