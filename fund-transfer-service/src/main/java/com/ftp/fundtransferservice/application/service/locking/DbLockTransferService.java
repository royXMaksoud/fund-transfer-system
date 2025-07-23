package com.ftp.fundtransferservice.application.service.locking;

import com.ftp.fundtransferservice.domain.ports.out.LockTransferPort;
import com.ftp.fundtransferservice.domain.ports.out.DbLockPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * DbLockTransferService is responsible for handling the locking mechanism
 * related to transfer operations using a database-level locking strategy.
 * It implements the LockTransferPort interface and uses DbLockPort to interact with the database.
 */
@Service
public class DbLockTransferService implements LockTransferPort {

    private final DbLockPort dbLockPort;

    /**
     * Constructs a DbLockTransferService with the specified database lock port.
     * This constructor injects the DbLockPort required to handle locking at the database level.
     *
     * @param dbLockPort the DbLockPort responsible for database-level locking operations
     */
    public DbLockTransferService(DbLockPort dbLockPort) {
        this.dbLockPort = dbLockPort;
    }

    /**
     * Locks the sender account using database-level locking.
     * This method is used to lock the sender's account during a transfer to prevent race conditions.
     *
     * @param senderId the UUID of the sender's account to lock
     */
    @Override
    public void lock(UUID senderId) {
        // Lock the sender's account at the database level
        dbLockPort.lockSender(senderId);
    }

    /**
     * Unlocks the sender account after the transfer is completed.
     * Currently, the method does not release the lock. This can be extended later if needed.
     *
     * @param senderId the UUID of the sender's account to unlock
     */

    @Override
    public void unlock(UUID senderId) {
        dbLockPort.unlockSender(senderId); // Implement the unlock method here
    }
}
