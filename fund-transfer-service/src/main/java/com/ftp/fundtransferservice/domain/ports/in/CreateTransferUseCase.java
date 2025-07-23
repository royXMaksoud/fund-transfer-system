package com.ftp.fundtransferservice.domain.ports.in;

import com.ftp.fundtransferservice.domain.model.Transfer;
import com.ftp.fundtransferservice.shared.constants.Currency;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * CreateTransferUseCase defines the contract for creating a new transfer between two accounts.
 * This interface contains the method to create a new transfer with the specified sender, receiver, amount, and currency.
 */
public interface CreateTransferUseCase {

    /**
     * Creates a new transfer from a sender to a receiver with the specified amount and currency.
     * The transfer is initiated based on the provided parameters.
     *
     * @param senderId the UUID of the sender's account
     * @param receiverId the UUID of the receiver's account
     * @param amount the amount of money to be transferred
     * @param currency the currency of the transfer (e.g., USD, EUR)
     * @return the created Transfer entity
     */
    Transfer createTransfer(UUID senderId, UUID receiverId, BigDecimal amount, Currency currency);
}
