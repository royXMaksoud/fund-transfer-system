package com.ftp.fundtransferservice.domain.ports.out;

import com.ftp.fundtransferservice.domain.model.Transfer;
import java.util.List;

/**
 * LoadTransfersPort defines the contract for loading transfer records.
 * This interface contains a method to retrieve all transfer records from the data source.
 */
public interface LoadTransfersPort {

    /**
     * Loads all transfer records from the data source.
     * This method fetches all the transfers stored in the system.
     *
     * @return a list of all Transfer entities
     */
    List<Transfer> loadAll();
}
