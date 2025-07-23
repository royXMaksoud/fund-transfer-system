package com.ftp.fundtransferservice.infrastructure.db.mappers;

import com.ftp.fundtransferservice.domain.model.Transfer;
import com.ftp.fundtransferservice.infrastructure.db.entities.TransferEntity;
import org.springframework.stereotype.Component;

/**
 * TransferMapper is responsible for mapping between domain model Transfer and database entity TransferEntity.
 * This class provides methods to map Transfer to TransferEntity and vice versa.
 */
@Component
public class TransferMapper {

    /**
     * Converts a domain model Transfer to a database entity TransferEntity.
     *
     * @param transfer the Transfer object to convert
     * @return the corresponding TransferEntity object
     * @throws IllegalArgumentException if transfer is null
     */
    public TransferEntity toEntity(Transfer transfer) {
        if (transfer == null) {
            throw new IllegalArgumentException("Transfer cannot be null");
        }

        return new TransferEntity(
                transfer.getId(),
                transfer.getSenderId(),
                transfer.getReceiverId(),
                transfer.getAmount(),
                transfer.getCurrency(),
                transfer.getStatus(),
                transfer.getCreatedAt()
        );
    }

    /**
     * Converts a database entity TransferEntity to a domain model Transfer.
     *
     * @param entity the TransferEntity object to convert
     * @return the corresponding Transfer domain object
     * @throws IllegalArgumentException if entity is null
     */
    public Transfer toDomain(TransferEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("TransferEntity cannot be null");
        }

        return new Transfer(
                entity.getId(),
                entity.getSenderId(),
                entity.getReceiverId(),
                entity.getAmount(),
                entity.getCurrency(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
