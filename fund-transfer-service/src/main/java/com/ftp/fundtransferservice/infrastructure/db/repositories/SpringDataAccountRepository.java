package com.ftp.fundtransferservice.infrastructure.db.repositories;

import com.ftp.fundtransferservice.infrastructure.db.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * SpringDataAccountRepository is a Spring Data JPA repository for managing AccountEntity.
 * This interface extends JpaRepository to provide basic CRUD operations.
 */
public interface SpringDataAccountRepository extends JpaRepository<AccountEntity, UUID> {

    /**
     * Finds an account by userId.
     * This method returns an Optional to handle cases where the account may not exist.
     *
     * @param userId the UUID of the user whose account is being retrieved
     * @return an Optional containing the AccountEntity if found, or empty if not found
     */
    Optional<AccountEntity> findByUserId(UUID userId);
}
