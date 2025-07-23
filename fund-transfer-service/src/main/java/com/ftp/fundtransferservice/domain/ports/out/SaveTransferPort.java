package com.ftp.fundtransferservice.domain.ports.out;

import com.ftp.fundtransferservice.domain.model.Transfer;

/**
 * SaveTransferPort defines the contract for saving transfer records.
 * This interface provides a method to persist transfer data in the system.
 */
public interface SaveTransferPort {

    /**
     * Saves the provided transfer entity to the data source.
     * The implementation will persist the transfer data (e.g., in a database).
     *
     * @param transfer the Transfer entity to be saved
     * @return the saved Transfer entity, including any updated details such as IDs
     */
    Transfer save(Transfer transfer);
}
