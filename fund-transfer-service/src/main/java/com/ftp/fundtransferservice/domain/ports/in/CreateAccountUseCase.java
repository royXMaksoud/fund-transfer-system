// CreateAccountUseCase.java
package com.ftp.fundtransferservice.domain.ports.in;

import com.ftp.fundtransferservice.domain.model.Account;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * CreateAccountUseCase defines the contract for creating a new account.
 * This interface contains the method to create a new account with a specified initial balance.
 */
public interface CreateAccountUseCase {

    /**
     * Creates a new account for a user with the specified initial balance.
     *
     * @param userId the UUID of the user who owns the account
     * @param balance the initial balance of the account
     * @return the created Account entity
     */
    Account createAccount(UUID userId, BigDecimal balance);
}
