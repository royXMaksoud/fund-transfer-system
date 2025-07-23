package com.ftp.fundtransferservice.infrastructure.db.mappers;

import com.ftp.fundtransferservice.domain.model.Account;
import com.ftp.fundtransferservice.infrastructure.db.entities.AccountEntity;

/**
 * AccountMapper is responsible for mapping between domain model Account and database entity AccountEntity.
 * This class provides static methods to map Account to AccountEntity and vice versa.
 */
public class AccountMapper {

    /**
     * Converts a domain model Account to a database entity AccountEntity.
     *
     * @param acc the domain Account object to convert
     * @return the corresponding AccountEntity object
     */
    public static AccountEntity toEntity(Account acc) {
        if (acc == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        AccountEntity entity = new AccountEntity();
        entity.setId(acc.getId());
        entity.setUserId(acc.getUserId());
        entity.setBalance(acc.getBalance());
        return entity;
    }

    /**
     * Converts a database entity AccountEntity to a domain model Account.
     *
     * @param entity the AccountEntity object to convert
     * @return the corresponding Account domain object
     */
    public static Account toDomain(AccountEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("AccountEntity cannot be null");
        }

        return new Account(entity.getId(), entity.getUserId(), entity.getBalance());
    }
}
