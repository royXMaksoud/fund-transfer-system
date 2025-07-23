// DeleteAccountUseCase.java
package com.ftp.fundtransferservice.domain.ports.in;

import java.util.UUID;

/**
 * DeleteAccountUseCase defines the contract for deleting an account.
 * This interface contains the method to delete an account based on the provided account ID.
 */
public interface DeleteAccountUseCase {

    /**
     * Deletes the account with the specified account ID.
     * The implementation will contain the logic for removing the account from the system.
     *
     * @param accountId the UUID of the account to be deleted
     */
    void deleteAccount(UUID accountId);
}
