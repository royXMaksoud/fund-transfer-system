package com.ftp.fundtransferservice.infrastructure.db.repositories;

import com.ftp.fundtransferservice.infrastructure.db.entities.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * SpringDataTransferRepository is a Spring Data JPA repository for managing TransferEntity.
 * This interface extends JpaRepository to provide basic CRUD operations for TransferEntity.
 */
public interface SpringDataTransferRepository extends JpaRepository<TransferEntity, UUID> {

    // You can add custom queries here if needed in the future. Example:
    // Optional<TransferEntity> findBySenderId(UUID senderId);
}
