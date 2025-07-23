package com.ftp.fundtransferservice.infrastructure.persistence.adapters;

import com.ftp.fundtransferservice.domain.model.Account;
import com.ftp.fundtransferservice.domain.ports.out.AccountRepositoryPort;
import com.ftp.fundtransferservice.infrastructure.db.entities.AccountEntity;
import com.ftp.fundtransferservice.infrastructure.db.mappers.AccountMapper;
import com.ftp.fundtransferservice.infrastructure.db.repositories.SpringDataAccountRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * AccountRepositoryAdapter is the implementation of AccountRepositoryPort interface,
 * responsible for managing account entities in the database using Spring Data JPA.
 * It provides the necessary methods for saving, finding, and deleting accounts.
 */
@Repository
public class AccountRepositoryAdapter implements AccountRepositoryPort {

    private final SpringDataAccountRepository repo;

    /**
     * Constructor to inject the SpringDataAccountRepository.
     *
     * @param repo the Spring Data JPA repository for account entities
     */
    public AccountRepositoryAdapter(SpringDataAccountRepository repo) {
        this.repo = repo;
    }

    /**
     * Saves the given Account object into the database.
     *
     * @param account the Account object to save
     * @return the saved Account object
     */
    @Override
    public Account save(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        AccountEntity entity = AccountMapper.toEntity(account);
        return AccountMapper.toDomain(repo.save(entity));
    }

    /**
     * Deletes the account by the given id.
     *
     * @param id the id of the account to delete
     * @throws IllegalArgumentException if the id is null
     */
    @Override
    public void deleteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        repo.deleteById(id);
    }

    /**
     * Finds an account by its id.
     *
     * @param id the id of the account to find
     * @return the found Account object or null if not found
     * @throws IllegalArgumentException if the id is null
     */
    @Override
    public Account findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return repo.findById(id).map(AccountMapper::toDomain).orElse(null);
    }

    /**
     * Finds all accounts from the database.
     *
     * @return a list of all Account objects
     */
    @Override
    public List<Account> findAll() {
        return repo.findAll().stream()
                .map(AccountMapper::toDomain)
                .collect(Collectors.toList());
    }
}
