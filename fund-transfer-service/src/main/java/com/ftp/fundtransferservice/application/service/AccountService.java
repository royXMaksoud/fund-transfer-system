// AccountService.java
package com.ftp.fundtransferservice.application.service;

import com.ftp.fundtransferservice.domain.model.Account;
import com.ftp.fundtransferservice.domain.ports.in.*;
import com.ftp.fundtransferservice.domain.ports.out.AccountRepositoryPort;
import com.ftp.fundtransferservice.shared.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service class responsible for managing account-related operations,
 * including creating, deleting, and loading accounts.
 * Implements use cases for creating, deleting, and loading accounts.
 */
@Service
public class AccountService implements
        CreateAccountUseCase,
        DeleteAccountUseCase,
        LoadAccountsUseCase {

    private final AccountRepositoryPort accountRepo;

    /**
     * Constructs an AccountService with the specified repository port.
     *
     * @param accountRepo the account repository port for data access
     */
    public AccountService(AccountRepositoryPort accountRepo) {
        this.accountRepo = accountRepo;
    }

    /**
     * Creates a new account for a user with the specified initial balance.
     *
     * @param userId  the UUID of the user who owns the account
     * @param balance the initial balance of the account
     * @return the created Account entity
     * @throws AppException if account creation fails or validation fails
     */
    @Override
    public Account createAccount(UUID userId, BigDecimal balance) {
        // Create a new account instance with the provided user ID and balance
        Account account = new Account(null, userId, balance);

        // Save the account to the repository and return the saved entity
        return accountRepo.save(account);
    }

    /**
     * Deletes an account by its UUID.
     * Throws an exception if the account does not exist.
     *
     * @param accountId the UUID of the account to delete
     * @throws AppException if the account does not exist
     */
    @Override
    public void deleteAccount(UUID accountId) {
        // Check if the account exists in the repository
        Account existing = accountRepo.findById(accountId);
        if (existing == null) {
            // Throw an exception if the account is not found

            throw new AppException("Account not found with ID" + accountId, "ACCOUNT_NOT_FOUND", HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        // Delete the account from the repository
        accountRepo.deleteById(accountId);
    }

    /**
     * Loads all existing accounts.
     *
     * @return a list of all Account entities
     */
    @Override
    public List<Account> loadAllAccounts() {
        // Fetch and return all accounts from the repository
        return accountRepo.findAll();
    }

    /**
     * Loads an account by its UUID.
     * Throws an exception if the account is not found.
     *
     * @param accountId the UUID of the account to load
     * @return the Account entity
     * @throws AppException if the account is not found
     */
    @Override
    public Account loadAccountById(UUID accountId) {
        // Fetch the account by ID
        Account existing = accountRepo.findById(accountId);
        if (existing == null) {
            // Throw an exception if the account is not found

            throw new AppException("Account not found with ID" + accountId, "ACCOUNT_NOT_FOUND", HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        return existing;
    }

    /**
     * Retrieves an account by its UUID without throwing an exception if not found.
     * If the account is not found, it returns null instead of throwing an exception.
     *
     * @param accountId the UUID of the account
     * @return the Account entity or null if not found
     */
    public Account getAccountById(UUID accountId) {
        // Try to find the account by ID, return null if not found
        return accountRepo.findById(accountId);
    }

    /**
     * Deletes an account by its UUID without prior existence check.
     * Deletes the account regardless of whether it exists or not.
     *
     * @param accountId the UUID of the account to delete
     */
    public void deleteAccountById(UUID accountId) {
        // Directly delete the account by ID without checking existence
        accountRepo.deleteById(accountId);
    }
}
