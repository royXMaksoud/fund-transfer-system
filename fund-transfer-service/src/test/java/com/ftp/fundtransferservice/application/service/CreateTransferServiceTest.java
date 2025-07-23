package com.ftp.fundtransferservice.application.service;

import com.ftp.fundtransferservice.domain.model.Account;
import com.ftp.fundtransferservice.domain.model.Transfer;
import com.ftp.fundtransferservice.domain.ports.out.*;
import com.ftp.fundtransferservice.shared.constants.Currency;
import com.ftp.fundtransferservice.shared.constants.TransferConstants;
import com.ftp.fundtransferservice.shared.exception.AppException;
import com.ftp.fundtransferservice.shared.exception.InsufficientBalanceException;
import com.ftp.fundtransferservice.shared.exception.InvalidTransferAmountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateTransferServiceTest {

    private SaveTransferPort saveTransferPort;
    private LockTransferPort lockTransferPort;
    private DbLockPort dbLockPort;
    private AccountRepositoryPort accountRepositoryPort;

    private CreateTransferService createTransferService;

    private UUID senderId;
    private UUID receiverId;
    private Account sender;
    private Account receiver;

    @BeforeEach
    void setUp() {
        saveTransferPort = mock(SaveTransferPort.class);
        lockTransferPort = mock(LockTransferPort.class);
        dbLockPort = mock(DbLockPort.class);
        accountRepositoryPort = mock(AccountRepositoryPort.class);

        createTransferService = new CreateTransferService(
                saveTransferPort, lockTransferPort, dbLockPort, accountRepositoryPort
        );

        senderId = UUID.randomUUID();
        receiverId = UUID.randomUUID();

        sender = new Account(senderId, UUID.randomUUID(), new BigDecimal("200.00"));
        receiver = new Account(receiverId, UUID.randomUUID(), new BigDecimal("50.00"));
    }

    @Test
    void shouldThrowException_whenTransferAmountIsInvalid() {
        BigDecimal amount = new BigDecimal("-10.00");

        assertThatThrownBy(() -> createTransferService.createTransfer(senderId, receiverId, amount, Currency.USD))
                .isInstanceOf(InvalidTransferAmountException.class);
    }

    @Test
    void shouldThrowException_whenAccountNotFound() {
        when(accountRepositoryPort.findById(senderId)).thenReturn(null);
        when(accountRepositoryPort.findById(receiverId)).thenReturn(receiver);

        BigDecimal amount = new BigDecimal("50.00");

        assertThatThrownBy(() -> createTransferService.createTransfer(senderId, receiverId, amount, Currency.USD))
                .isInstanceOf(AppException.class);
    }

    @Test
    void shouldThrowException_whenBalanceIsInsufficient() {
        sender.setBalance(new BigDecimal("30.00"));
        when(accountRepositoryPort.findById(senderId)).thenReturn(sender);
        when(accountRepositoryPort.findById(receiverId)).thenReturn(receiver);

        BigDecimal amount = new BigDecimal("100.00");

        assertThatThrownBy(() -> createTransferService.createTransfer(senderId, receiverId, amount, Currency.USD))
                .isInstanceOf(InsufficientBalanceException.class);
    }

    @Test
    void shouldTransferFundsSuccessfully() {
        when(accountRepositoryPort.findById(senderId)).thenReturn(sender);
        when(accountRepositoryPort.findById(receiverId)).thenReturn(receiver);

        BigDecimal amount = new BigDecimal("100.00");

        Transfer expectedTransfer = new Transfer(
                UUID.randomUUID(), senderId, receiverId, amount, Currency.USD, null, null
        );
        when(saveTransferPort.save(any(Transfer.class))).thenReturn(expectedTransfer);

        Transfer result = createTransferService.createTransfer(senderId, receiverId, amount, Currency.USD);

        assertThat(result).isNotNull();
        assertThat(sender.getBalance()).isEqualByComparingTo("100.00");
        assertThat(receiver.getBalance()).isEqualByComparingTo("150.00");

        verify(accountRepositoryPort).save(sender);
        verify(accountRepositoryPort).save(receiver);
        verify(saveTransferPort).save(any(Transfer.class));
        verify(lockTransferPort).lock(senderId);
        verify(lockTransferPort).unlock(senderId);
        verify(dbLockPort).lockSender(senderId);
    }
}
