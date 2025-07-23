// file: application/service/locking/ReentrantLockTransferService.java
package com.ftp.fundtransferservice.application.service.locking;

import com.ftp.fundtransferservice.domain.ports.out.LockTransferPort;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLockTransferService is responsible for locking and unlocking transfer operations
 * using ReentrantLock to prevent race conditions. It ensures that only one thread
 * can operate on a specific sender account at any given time.
 */
@Service
public class ReentrantLockTransferService implements LockTransferPort {

    // Map to hold locks for each sender account, using senderId as the key
    private final Map<UUID, ReentrantLock> locks = new ConcurrentHashMap<>();

    /**
     * Locks the sender's account using a ReentrantLock to prevent concurrent access.
     * If the lock does not exist, it is created and locked.
     *
     * @param senderId the UUID of the sender's account to lock
     */
    @Override
    public void lock(UUID senderId) {
        // Get the lock associated with the senderId, or create a new one if it does not exist
        ReentrantLock lock = locks.computeIfAbsent(senderId, id -> new ReentrantLock());

        // Lock the sender account to ensure no other thread can access it concurrently
        lock.lock();
    }

    /**
     * Unlocks the sender's account after the operation is completed.
     * Ensures that the current thread holds the lock before attempting to unlock.
     *
     * @param senderId the UUID of the sender's account to unlock
     */
    @Override
    public void unlock(UUID senderId) {
        // Retrieve the lock for the senderId
        ReentrantLock lock = locks.get(senderId);

        // Check if the lock exists and if the current thread holds the lock
        if (lock != null && lock.isHeldByCurrentThread()) {
            // Unlock the account if the current thread holds the lock
            lock.unlock();
        }
    }
}
