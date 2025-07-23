// LoadAccountsUseCase.java
package com.ftp.fundtransferservice.domain.ports.in;

import com.ftp.fundtransferservice.domain.model.Account;

import java.util.List;
import java.util.UUID;

/**
 * LoadAccountsUseCase defines the contract for loading accounts.
 * This interface provides methods to retrieve all accounts or a specific account by its ID.
 */
public interface LoadAccountsUseCase {

    /**
     * Retrieves all accounts from the system.
     * This method is used to get a list of all accounts present in the system.
     *
     * @return a list of all Account entities
     */
    List<Account> loadAllAccounts();

    /**
     * Loads an account by its unique identifier (UUID).
     * This method is used to retrieve a specific account by its ID.
     *
     * @param accountId the UUID of the account to load
     * @return the Account entity corresponding to the given accountId
     */
    Account loadAccountById(UUID accountId);
}
