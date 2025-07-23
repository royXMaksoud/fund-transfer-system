package com.ftp.fundtransferservice.domain.ports.in;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * TransferFundsUseCase defines the contract for transferring funds between two accounts.
 * This interface contains the method to transfer funds from one account to another.
 */
public interface TransferFundsUseCase {

    /**
     * Transfers a specified amount of money from one account to another.
     *
     * @param fromAccountId the UUID of the sender's account
     * @param toAccountId the UUID of the receiver's account
     * @param amount the amount of money to be transferred
     * @throws IllegalArgumentException if the amount is negative or invalid
     */
    void transfer(UUID fromAccountId, UUID toAccountId, BigDecimal amount);
}
