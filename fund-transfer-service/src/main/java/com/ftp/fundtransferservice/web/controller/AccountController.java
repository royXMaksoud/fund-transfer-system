/**
 * AccountController is responsible for handling requests related to user accounts.
 * It provides endpoints to:
 * 1. Create a new account for a specific user with an initial balance.
 * 2. Retrieve an account's details by its unique account ID.
 * 3. Delete an account by its ID.
 *
 * The service is secured using JWT authentication (bearer token).
 * The controller leverages logging for monitoring and debugging actions.
 */
package com.ftp.fundtransferservice.web.controller;

import com.ftp.fundtransferservice.application.service.AccountService;
import com.ftp.fundtransferservice.domain.model.Account;
import com.ftp.fundtransferservice.web.dto.request.AccountRequest;
import com.ftp.fundtransferservice.web.dto.response.AccountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    private final AccountService accountService;
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class); // Logger for logging the actions

    /**
     * Constructor to inject the AccountService dependency.
     *
     * @param accountService The service that handles business logic for accounts.
     */
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Creates a new account for a user with an initial balance.
     * This endpoint accepts the user ID and initial balance to create the account.
     *
     * @param request The account request containing user ID and initial balance.
     * @return The created account response with ID, user ID, and balance.
     */
    @PostMapping
    @Operation(
            summary = "Create a new account",
            description = "Creates a new account for a specific user with an initial balance. "
                    + "The user ID must be provided in the request along with the desired starting balance."
    )
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest request) {
        logger.info("Creating new account for userId: {}", request.getUserId()); // Logging account creation
        Account created = accountService.createAccount(request.getUserId(), request.getBalance());
        logger.info("Account created successfully with ID: {}", created.getId()); // Log account creation success
        return ResponseEntity.ok(new AccountResponse(created.getId(), created.getUserId(), created.getBalance()));
    }

    /**
     * Retrieves the account details by its unique ID.
     * This endpoint fetches the account's ID, user ID, and current balance.
     *
     * @param id The unique ID of the account.
     * @return The account response containing the account's details.
     */
    @Operation(
            summary = "Retrieve an account by ID",
            description = "Fetches the account details using the unique account ID. "
                    + "This endpoint returns the account's ID, user ID, and current balance."
    )
    @GetMapping("/{id}/details")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable UUID id) {
        logger.info("Fetching account with ID: {}", id); // Log account fetch attempt
        Account account = accountService.getAccountById(id);
        if (account == null) {
            logger.error("Account not found with ID: {}", id); // Log error if account is not found
            return ResponseEntity.notFound().build();
        }
        logger.info("Account found with ID: {}", id); // Log success if account is found
        return ResponseEntity.ok(new AccountResponse(account.getId(), account.getUserId(), account.getBalance()));
    }

    /**
     * Deletes the account by its unique ID.
     * This operation removes the account permanently from the system.
     *
     * @param id The unique ID of the account to be deleted.
     * @return A no content response indicating successful deletion.
     */
    @DeleteMapping("/{id}/remove")
    @Operation(
            summary = "Delete an account",
            description = "Deletes an existing account using its unique ID. "
                    + "After deletion, the account can no longer be accessed."
    )
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID id) {
        logger.info("Deleting account with ID: {}", id); // Log account deletion attempt
        accountService.deleteAccountById(id);
        logger.info("Account with ID: {} deleted successfully.", id); // Log deletion success
        return ResponseEntity.noContent().build();
    }
}
