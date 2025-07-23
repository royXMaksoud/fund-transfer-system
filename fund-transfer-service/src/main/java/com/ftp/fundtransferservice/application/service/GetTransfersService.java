package com.ftp.fundtransferservice.application.service;

import com.ftp.fundtransferservice.domain.model.Transfer;
import com.ftp.fundtransferservice.domain.ports.in.GetTransfersUseCase;
import com.ftp.fundtransferservice.domain.ports.out.LoadTransfersPort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for retrieving all transfer records.
 * This service delegates the data fetching to the {@link LoadTransfersPort} repository port.
 * It is used to provide a list of all transfers in the system.
 */
@Service
public class GetTransfersService implements GetTransfersUseCase {

    private final LoadTransfersPort loadTransfersPort;

    /**
     * Constructs the GetTransfersService with the required port.
     * This constructor injects the dependency required to load transfer data.
     *
     * @param loadTransfersPort port responsible for loading transfer data
     */
    public GetTransfersService(LoadTransfersPort loadTransfersPort) {
        this.loadTransfersPort = loadTransfersPort;
    }

    /**
     * Returns a list of all transfer entities.
     * This method delegates the call to the repository port to load all transfers.
     *
     * @return list of {@link Transfer}
     */
    @Override
    public List<Transfer> getTransfers() {
        return loadTransfersPort.loadAll(); // Fetches all transfer records from the repository
    }
}
