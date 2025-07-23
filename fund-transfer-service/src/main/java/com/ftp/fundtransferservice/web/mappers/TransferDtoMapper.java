package com.ftp.fundtransferservice.web.mappers;

import com.ftp.fundtransferservice.domain.model.Transfer;
import com.ftp.fundtransferservice.web.dto.response.TransferResponse;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TransferDtoMapper {

    private static final Logger log = LoggerFactory.getLogger(TransferDtoMapper.class); // Adding logging

    /**
     * Converts the domain model Transfer to TransferResponse DTO.
     *
     * @param transfer The Transfer domain model object.
     * @return A TransferResponse DTO object containing transfer details.
     */
    public TransferResponse toResponse(Transfer transfer) {
        if (transfer == null) {
            log.warn("Attempted to map a null transfer object to TransferResponse.");
            return null;  // Return null or throw an exception depending on how you want to handle it
        }

        // Mapping the fields from the Transfer domain model to TransferResponse DTO
        return new TransferResponse(
                transfer.getId(),                       // Transfer unique identifier
                transfer.getSenderId(),                 // Sender account ID
                transfer.getReceiverId(),               // Receiver account ID
                transfer.getAmount(),                   // Transfer amount
                transfer.getCurrency().name(),          // Currency code (enum to String)
                transfer.getStatus(),                   // Transfer status (e.g., COMPLETED, PENDING)
                transfer.getCreatedAt()                 // Transfer timestamp
        );
    }
}
