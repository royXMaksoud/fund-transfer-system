package com.ftp.fundtransferservice.web.controller;

import com.ftp.fundtransferservice.domain.model.Transfer;
import com.ftp.fundtransferservice.domain.ports.in.CreateTransferUseCase;
import com.ftp.fundtransferservice.domain.ports.in.GetTransfersUseCase;
import com.ftp.fundtransferservice.shared.exception.InsufficientBalanceException;
import com.ftp.fundtransferservice.web.dto.request.TransferRequest;
import com.ftp.fundtransferservice.web.dto.response.TransferResponse;
import com.ftp.fundtransferservice.web.mappers.TransferDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TransferController is responsible for handling requests related to fund transfers.
 * This controller exposes endpoints to:
 * 1. Retrieve all fund transfers.
 * 2. Create a new fund transfer between two accounts.
 *
 * The service is secured using JWT authentication (bearer token).
 * Logging is employed for monitoring and debugging the operations.
 */
@RestController
@RequestMapping("/transfers")
@SecurityRequirement(name = "bearerAuth") // Secures the endpoints with JWT authentication
public class TransferController {

    private static final Logger log = LoggerFactory.getLogger(TransferController.class); // Logger for logging actions

    private final CreateTransferUseCase createTransferUseCase;
    private final TransferDtoMapper transferDtoMapper;
    private final GetTransfersUseCase getTransfersUseCase;

    /**
     * Constructor to inject the required dependencies.
     *
     * @param createTransferUseCase The use case that handles fund transfer creation logic.
     * @param getTransfersUseCase The use case that fetches all transfers.
     * @param transferDtoMapper A mapper for transforming Transfer entities to TransferResponse DTOs.
     */
    public TransferController(CreateTransferUseCase createTransferUseCase,
                              GetTransfersUseCase getTransfersUseCase,
                              TransferDtoMapper transferDtoMapper) {
        this.createTransferUseCase = createTransferUseCase;
        this.transferDtoMapper = transferDtoMapper;
        this.getTransfersUseCase = getTransfersUseCase;
    }

    /**
     * Retrieves all the fund transfers.
     *
     * This endpoint fetches all transfer transactions in the system.
     * It returns a list of transfers along with their details.
     *
     * @return A ResponseEntity containing a list of TransferResponse DTOs.
     */
    @GetMapping
    @Operation(
            summary = "Get all transfers",
            description = "Retrieves a list of all fund transfer transactions. Requires a valid JWT token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of transfers successfully retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token"),
            @ApiResponse(responseCode = "500", description = "Server error while fetching transfers")
    })
    public ResponseEntity<List<TransferResponse>> getAllTransfers() {
        List<Transfer> transfers = getTransfersUseCase.getTransfers();
        List<TransferResponse> responses = transfers.stream()
                .map(transferDtoMapper::toResponse) // Mapping each transfer entity to TransferResponse
                .collect(Collectors.toList()); // Collecting the list of responses
        return ResponseEntity.ok(responses); // Returning the list as a response
    }

    /**
     * Creates a new fund transfer between two accounts.
     *
     * This endpoint initiates a transfer between the sender and the receiver with the provided amount and currency.
     * The request includes senderId, receiverId, amount, and currency.
     *
     * @param request The TransferRequest DTO containing transfer details.
     * @return A ResponseEntity containing the TransferResponse DTO with transfer details.
     */
    @PostMapping
    @Operation(
            summary = "Create a new fund transfer",
            description = "Creates a new transfer between two accounts."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid transfer amount"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TransferResponse> createTransfer(@Valid @RequestBody TransferRequest request) {
        log.info("Starting transfer from senderId={} to receiverId={} for amount={}",
                request.getSenderId(), request.getReceiverId(), request.getAmount());

        // Optional: Simulating delay for testing concurrency (can be removed after testing)
        try {
            Thread.sleep(2000); // Simulating a 2-second delay for testing
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Resetting the interruption status if interrupted
        }

        // Create transfer and handle exceptions like Insufficient Balance
        Transfer transfer = createTransferUseCase.createTransfer(
                request.getSenderId(),
                request.getReceiverId(),
                request.getAmount(),
                request.getCurrency()
        );

        log.info("Transfer successful: {}", transfer.getId());

        return ResponseEntity.ok(transferDtoMapper.toResponse(transfer)); // Returning the transfer details as a response
    }
}
