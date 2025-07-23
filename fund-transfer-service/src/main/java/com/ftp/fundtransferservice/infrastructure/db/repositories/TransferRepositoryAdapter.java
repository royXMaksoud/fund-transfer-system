package com.ftp.fundtransferservice.infrastructure.db.repositories;

import com.ftp.fundtransferservice.domain.model.Transfer;
import com.ftp.fundtransferservice.domain.ports.out.LoadTransfersPort;
import com.ftp.fundtransferservice.domain.ports.out.SaveTransferPort;
import com.ftp.fundtransferservice.infrastructure.db.mappers.TransferMapper;
import org.springframework.stereotype.Component;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TransferRepositoryAdapter implements the SaveTransferPort and LoadTransfersPort interfaces
 * and provides the implementation for saving and loading transfer records from the database.
 */
@Component
public class TransferRepositoryAdapter implements SaveTransferPort, LoadTransfersPort {

    private final SpringDataTransferRepository repository;
    private final TransferMapper mapper;

    /**
     * Constructor for initializing the repository and mapper.
     *
     * @param repository the Spring Data repository for transfers
     * @param mapper the TransferMapper for converting between domain and entity
     */
    public TransferRepositoryAdapter(SpringDataTransferRepository repository, TransferMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Saves the transfer entity in the database.
     * Converts the Transfer domain object to a TransferEntity before saving it.
     *
     * @param transfer the Transfer object to save
     * @return the saved Transfer object
     * @throws DataAccessException if there is an issue accessing the database
     */
    @Override
    public Transfer save(Transfer transfer) {
        try {
            return mapper.toDomain(
                    repository.save(mapper.toEntity(transfer))
            );
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to save transfer", e) {};
        }
    }

    /**
     * Loads all transfer records from the database.
     * Converts the TransferEntity objects from the database to Transfer domain objects.
     *
     * @return a list of Transfer objects
     */
    @Override
    public List<Transfer> loadAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
