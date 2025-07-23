package com.ftp.fundtransferservice.domain.ports.out;

import java.util.UUID;

/**
 * LockTransferPort defines the contract for acquiring and releasing locks
 * during the transfer of funds. This interface ensures safe and atomic transfers
 * by locking the sender's account to prevent race conditions.
 */
public interface LockTransferPort {

    /**
     * Acquires a lock for a specific sender to ensure safe transfer.
     * This prevents any other operation from modifying the sender's account balance
     * while the transfer is in progress.
     *
     * @param senderId The ID of the user who is sending money
     */
    void lock(UUID senderId);

    /**
     * Releases the lock after the transfer is completed.
     * This allows other operations to access the sender's account balance after the transfer.
     *
     * @param senderId The ID of the user who is sending money
     */
    void unlock(UUID senderId);
}
