package com.ftp.fundtransferservice.infrastructure.db.locking;

import com.ftp.fundtransferservice.domain.ports.out.DbLockPort;
import com.ftp.fundtransferservice.infrastructure.db.entities.AccountEntity;
import com.ftp.fundtransferservice.shared.exception.AppException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DbLockAdapter implements the DbLockPort interface and handles database-level locking
 * for the sender's account during fund transfer transactions.
 * It ensures safe concurrent access by acquiring a pessimistic write lock.
 */
@Component
public class DbLockAdapter implements DbLockPort {

    private final EntityManager entityManager;

    /**
     * Constructor to inject EntityManager for JPA operations.
     *
     * @param entityManager the JPA entity manager
     */
    public DbLockAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Locks the sender's account using pessimistic write lock to prevent race conditions during fund transfers.
     * This ensures that only one transaction can access the sender's account for writing at a time.
     *
     * @param accountId the UUID of the account to lock
     * @throws AppException if the account is not found
     */
    @Override
    @Transactional
    public void lockSender(UUID accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }

        // Lock the sender account using pessimistic write lock
        List<AccountEntity> results = entityManager
                .createQuery("SELECT a FROM AccountEntity a WHERE a.id = :accountId", AccountEntity.class)
                .setParameter("accountId", accountId)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultList();

        if (results.isEmpty()) {

            throw new AppException("Account not found with ID" + accountId, "ACCOUNT_NOT_FOUND", HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
    }

    @Override
    public void unlockSender(UUID senderId) {
        if (senderId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }

        // Unlock the sender's account by ensuring the entity is managed by the EntityManager
        // and releasing the lock on it.
        // JPA handles the release of locks automatically, so this may not be necessary,
        // but you can explicitly fetch and refresh the entity to release any locks.

        // You can explicitly flush or detach if needed, but this may depend on how your
        // transaction and entity manager are configured.

        entityManager.flush(); // Ensure any pending changes are committed before unlocking

        // Optionally, you can use `entityManager.clear()` to detach the entities
        // and release any held locks. This may be useful in long-running transactions.
        entityManager.clear();
    }

}
