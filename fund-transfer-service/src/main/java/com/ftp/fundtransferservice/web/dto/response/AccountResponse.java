package com.ftp.fundtransferservice.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for returning account details in API responses.
 * Contains account ID, user ID (owner), and current balance.
 */
@Schema(description = "DTO used for returning account information")
public class AccountResponse {

    @Schema(description = "Unique identifier of the account", example = "a1f2c3e4-d567-8901-2345-6789abcdef01", required = true)
    private final UUID id;

    @Schema(description = "Unique identifier of the user who owns the account", example = "b2f4d3e6-a789-1234-5678-90abcdef1234", required = true)
    private final UUID userId;

    @Schema(description = "Current balance of the account", example = "1000.50", required = true)
    private final BigDecimal balance;

    public AccountResponse(UUID id, UUID userId, BigDecimal balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
