package com.ftp.fundtransferservice.web.dto.request;

import com.ftp.fundtransferservice.shared.constants.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * TransferRequest is a Data Transfer Object (DTO) used for initiating a fund transfer.
 * This DTO includes all the necessary information to perform a transfer:
 * senderId, receiverId, amount, and currency.
 */
public class TransferRequest {

    /**
     * The unique identifier for the sender of the transfer.
     * This field is required to identify the account initiating the transfer.
     */
    @NotNull(message = "senderId must not be null")
    @Schema(description = "UUID of the sender's account", example = "7a1b3f27-cc93-4f88-8e4d-02fd3bbfdc45", required = true)
    private UUID senderId;

    /**
     * The unique identifier for the receiver of the transfer.
     * This field is required to identify the account receiving the transfer.
     */
    @NotNull(message = "receiverId must not be null")
    @Schema(description = "UUID of the receiver's account", example = "b3f6a2f2-3a7d-42db-a5d2-1e1e0157c9c9", required = true)
    private UUID receiverId;

    /**
     * The amount of money to be transferred.
     * This field is required and must be a valid monetary value.
     */
    @NotNull(message = "amount must not be null")
    @DecimalMin(value = "0.01", inclusive = true, message = "amount must be greater than zero")
    @Schema(description = "Amount of money to be transferred", example = "150.75", required = true)
    private BigDecimal amount;

    /**
     * The currency of the transfer.
     * This field is required and ensures that the transfer is performed in a specified currency.
     */
    @NotNull(message = "currency must not be null")
    @Schema(description = "Currency of the transfer (USD, EUR, etc.)", example = "USD", required = true)
    private Currency currency; // ✅ Enum, not String to ensure type safety

    /**
     * Gets the sender ID associated with the transfer.
     *
     * @return the sender ID
     */
    public UUID getSenderId() {
        return senderId;
    }

    /**
     * Gets the receiver ID associated with the transfer.
     *
     * @return the receiver ID
     */
    public UUID getReceiverId() {
        return receiverId;
    }

    /**
     * Gets the amount of money to be transferred.
     *
     * @return the transfer amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Gets the currency of the transfer.
     *
     * @return the transfer currency
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Sets the sender ID for the transfer.
     *
     * @param senderId the sender's account ID
     */
    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    /**
     * Sets the receiver ID for the transfer.
     *
     * @param receiverId the receiver's account ID
     */
    public void setReceiverId(UUID receiverId) {
        this.receiverId = receiverId;
    }

    /**
     * Sets the amount to be transferred.
     *
     * @param amount the amount of money to transfer
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Sets the currency for the transfer.
     *
     * @param currency the currency for the transfer
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
