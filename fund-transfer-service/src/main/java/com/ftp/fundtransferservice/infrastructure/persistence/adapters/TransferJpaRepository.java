package com.ftp.fundtransferservice.infrastructure.persistence.adapters;

import com.ftp.fundtransferservice.infrastructure.db.entities.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * TransferJpaRepository is a Spring Data JPA repository for managing TransferEntity.
 * This interface extends JpaRepository to provide basic CRUD operations for TransferEntity.
 */
public interface TransferJpaRepository extends JpaRepository<TransferEntity, UUID> {

    /**
     * Retrieves a page of transfer records filtered by senderId.
     *
     * @param senderId the UUID of the sender
     * @param pageable the pageable object containing page request info
     * @return a page of transfer records for the given senderId
     */
    Page<TransferEntity> findBySenderId(UUID senderId, Pageable pageable);

    /**
     * Retrieves a page of transfer records filtered by receiverId.
     *
     * @param receiverId the UUID of the receiver
     * @param pageable the pageable object containing page request info
     * @return a page of transfer records for the given receiverId
     */
    Page<TransferEntity> findByReceiverId(UUID receiverId, Pageable pageable);

    /**
     * Retrieves a page of transfer records filtered by status.
     *
     * @param status the status of the transfer
     * @param pageable the pageable object containing page request info
     * @return a page of transfer records for the given status
     */
    Page<TransferEntity> findByStatus(String status, Pageable pageable);
}
