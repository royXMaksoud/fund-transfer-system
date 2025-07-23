package com.ftp.fundtransferservice.domain.ports.out;

import com.ftp.fundtransferservice.domain.model.Account;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * AccountRepositoryPort defines the contract for accessing account data.
 * This interface provides methods for saving, deleting, and retrieving account data from the data source.
 */
public interface AccountRepositoryPort {

    /**
     * Saves the provided account entity to the data source.
     *
     * @param account the account to save
     * @return the saved Account entity
     */
    Account save(Account account);

    /**
     * Deletes the account with the specified ID from the data source.
     * Throws IllegalArgumentException if the accountId is null.
     *
     * @param id the UUID of the account to delete
     * @throws IllegalArgumentException if the accountId is null
     */
    void deleteById(UUID id);

    /**
     * Finds an account by its unique identifier (UUID).
     * Throws IllegalArgumentException if the accountId is null.
     *
     * @param id the UUID of the account to find
     * @return the Account entity if found, otherwise null
     * @throws IllegalArgumentException if the accountId is null
     */
    Account findById(UUID id);

    /**
     * Retrieves all accounts from the data source.
     *
     * @return a list of all Account entities
     */
    List<Account> findAll();
}
