package com.ftp.fundtransferservice.domain.ports.in;

import com.ftp.fundtransferservice.domain.model.Transfer;
import java.util.List;

/**
 * GetTransfersUseCase defines the contract for retrieving all the transfer records.
 * This interface contains the method to fetch a list of all transfers in the system.
 */
public interface GetTransfersUseCase {

    /**
     * Retrieves a list of all transfer entities.
     * The implementation will fetch the transfer data from the database or other data source.
     *
     * @return a list of all transfers in the system
     */
    List<Transfer> getTransfers();
}
