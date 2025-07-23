package com.ftp.fundtransferservice.domain.ports.out;

import java.util.UUID;

/**
 * DbLockPort defines the contract for locking a sender account in the database.
 * This interface contains a method to lock the sender's account to prevent race conditions during the transfer.
 */
public interface DbLockPort {

    /**
     * Locks the sender's account in the database to avoid race conditions.
     * The account is locked until the transfer operation is completed.
     *
     * @param senderId the UUID of the sender's account to lock
     */
    void lockSender(UUID senderId);

    void unlockSender(UUID senderId);

}
