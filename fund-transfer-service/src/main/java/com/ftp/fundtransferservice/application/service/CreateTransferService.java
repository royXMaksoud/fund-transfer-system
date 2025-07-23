package com.ftp.fundtransferservice.application.service;

import com.ftp.fundtransferservice.domain.model.Account;
import com.ftp.fundtransferservice.domain.model.Transfer;
import com.ftp.fundtransferservice.domain.ports.in.CreateTransferUseCase;
import com.ftp.fundtransferservice.domain.ports.out.LockTransferPort;
import com.ftp.fundtransferservice.domain.ports.out.AccountRepositoryPort;
import com.ftp.fundtransferservice.domain.ports.out.DbLockPort;
import com.ftp.fundtransferservice.domain.ports.out.SaveTransferPort;
import com.ftp.fundtransferservice.shared.constants.Currency;
import com.ftp.fundtransferservice.shared.constants.TransferStatus;
import com.ftp.fundtransferservice.shared.constants.TransferConstants;  // Import TransferConstants
import com.ftp.fundtransferservice.shared.exception.AppException;
import com.ftp.fundtransferservice.shared.exception.InsufficientBalanceException;
import com.ftp.fundtransferservice.shared.exception.InvalidTransferAmountException;  // Custom exception for invalid transfer amount
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * CreateTransferService
 *
 * This service is responsible for executing secure and atomic fund transfers between accounts.
 * It ensures:
 * - Proper validation of sender and receiver accounts.
 * - Balance check to prevent overdrafts.
 * - Concurrency protection using:
 *     In-memory locking (LockTransferPort).
 *     Database-level locking (DbLockPort).
 * - Safe update of account balances.
 * - Transfer record creation and persistence.
 * - Releasing all locks after operation.
 */
@Service
public class CreateTransferService implements CreateTransferUseCase {

    private final SaveTransferPort saveTransferPort;
    private final LockTransferPort lockTransferPort;
    private final DbLockPort dbLockPort;
    private final AccountRepositoryPort accountRepositoryPort;

    private static final Logger log = LoggerFactory.getLogger(CreateTransferService.class);

    public CreateTransferService(SaveTransferPort saveTransferPort,
                                 LockTransferPort lockTransferPort,
                                 DbLockPort dbLockPort,
                                 AccountRepositoryPort accountRepositoryPort) {
        this.saveTransferPort = saveTransferPort;
        this.lockTransferPort = lockTransferPort;
        this.dbLockPort = dbLockPort;
        this.accountRepositoryPort = accountRepositoryPort;
    }

    @Override
    @Transactional
    public Transfer createTransfer(UUID senderAccountId, UUID receiverAccountId, BigDecimal amount, Currency currency) {
        // Step 1: Validate transfer amount using TransferConstants
        if (amount.compareTo(TransferConstants.MIN_TRANSFER_AMOUNT) < 0 || amount.compareTo(TransferConstants.MAX_TRANSFER_AMOUNT) > 0) {
            log.error("Invalid transfer amount {} for sender account {}. Must be between {} and {}.",
                    amount, senderAccountId, TransferConstants.MIN_TRANSFER_AMOUNT, TransferConstants.MAX_TRANSFER_AMOUNT);
            throw new InvalidTransferAmountException("Transfer amount must be between " +
                    TransferConstants.MIN_TRANSFER_AMOUNT + " and " + TransferConstants.MAX_TRANSFER_AMOUNT);
        }

        // Step 2: Acquire in-memory lock to protect against concurrent access
        lockTransferPort.lock(senderAccountId);

        try {
            // Step 3: Lock sender in the database to avoid race conditions
            dbLockPort.lockSender(senderAccountId);

            // Step 4: Fetch sender and receiver accounts from repository
            Account sender = accountRepositoryPort.findById(senderAccountId);
            Account receiver = accountRepositoryPort.findById(receiverAccountId);

            // Step 5: Validate both accounts exist
            if (sender == null || receiver == null) {

                throw new AppException("Sender or receiver not found" , "ACCOUNT_NOT_FOUND", HttpStatus.NOT_FOUND, LocalDateTime.now());
            }

            // Step 6: Check sender has enough balance
            if (sender.getBalance().compareTo(amount) < 0) {
                log.error("Insufficient balance for sender account {} to transfer {}",
                        senderAccountId, amount);
                throw new InsufficientBalanceException("Insufficient balance for transfer");
            }

            // Step 7: Adjust balances
            sender.setBalance(sender.getBalance().subtract(amount));
            receiver.setBalance(receiver.getBalance().add(amount));

            // Step 8: Save updated account states
            accountRepositoryPort.save(sender);
            accountRepositoryPort.save(receiver);

            // Step 9: Create and persist transfer record
            Transfer transfer = new Transfer(
                    UUID.randomUUID(),
                    senderAccountId,
                    receiverAccountId,
                    amount,
                    currency,
                    TransferStatus.COMPLETED,
                    LocalDateTime.now()
            );

            log.info("Transfer completed successfully from {} to {} | Amount: {} {}",
                    senderAccountId, receiverAccountId, amount, currency);

            return saveTransferPort.save(transfer);

        } finally {
            // Step 10: Always release in-memory lock and database lock
            lockTransferPort.unlock(senderAccountId);
            dbLockPort.unlockSender(senderAccountId);  // Ensure unlocking the sender's account in case of exception
        }
    }
}
