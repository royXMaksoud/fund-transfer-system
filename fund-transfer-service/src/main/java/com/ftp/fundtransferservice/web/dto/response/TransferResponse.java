package com.ftp.fundtransferservice.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ftp.fundtransferservice.shared.constants.TransferStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Response DTO representing the result of a transfer operation.")
public class TransferResponse {

    @Schema(description = "Unique identifier for the transfer", example = "d5b5c0c1-e3d7-4e55-b61f-4b9e6d4e2bfa", required = true)
    private final UUID id;

    @Schema(description = "Unique identifier for the sender account", example = "7a1b3f27-cc93-4f88-8e4d-02fd3bbfdc45", required = true)
    private final UUID senderId;

    @Schema(description = "Unique identifier for the receiver account", example = "b3f6a2f2-3a7d-42db-a5d2-1e1e0157c9c9", required = true)
    private final UUID receiverId;

    @Schema(description = "The amount transferred", example = "250.00", required = true)
    private final BigDecimal amount;

    @Schema(description = "Currency code of the transfer", example = "USD", required = true)
    private final String currency;

    @Schema(description = "Status of the transfer", example = "COMPLETED", required = true)
    private final TransferStatus status;

    @Schema(description = "Timestamp when the transfer was created", example = "2025-07-20T14:30:00", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime createdAt;

    public TransferResponse(UUID id, UUID senderId, UUID receiverId,
                            BigDecimal amount, String currency,
                            TransferStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public UUID getReceiverId() {
        return receiverId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isTransferSuccessful() {
        return this.status == TransferStatus.COMPLETED;
    }
}
