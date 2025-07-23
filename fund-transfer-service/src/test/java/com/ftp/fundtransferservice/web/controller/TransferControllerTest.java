package com.ftp.fundtransferservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftp.fundtransferservice.domain.model.Transfer;
import com.ftp.fundtransferservice.domain.ports.in.CreateTransferUseCase;
import com.ftp.fundtransferservice.domain.ports.in.GetTransfersUseCase;
import com.ftp.fundtransferservice.shared.constants.Currency;
import com.ftp.fundtransferservice.shared.constants.TransferStatus;
import com.ftp.fundtransferservice.shared.exception.AppException;
import com.ftp.fundtransferservice.shared.exception.InsufficientBalanceException;
import com.ftp.fundtransferservice.web.dto.request.TransferRequest;
import com.ftp.fundtransferservice.web.dto.response.TransferResponse;
import com.ftp.fundtransferservice.web.mappers.TransferDtoMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;

@Import(com.ftp.fundtransferservice.web.exception.GlobalExceptionHandler.class)
@WebMvcTest(TransferController.class)
@AutoConfigureMockMvc(addFilters = false)

class TransferControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private CreateTransferUseCase createTransferUseCase;
    @MockBean private GetTransfersUseCase getTransfersUseCase;
    @MockBean private TransferDtoMapper transferDtoMapper;

    @MockBean private com.ftp.fundtransferservice.infrastructure.security.JwtTokenProvider jwtTokenProvider;
    @MockBean private com.ftp.fundtransferservice.infrastructure.security.JwtAuthenticationFilter jwtAuthenticationFilter;

    @WithMockUser
    @Test
    void shouldReturn200_whenTransferIsSuccessful() throws Exception {
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("100.00");

        TransferRequest request = new TransferRequest();
        request.setSenderId(senderId);
        request.setReceiverId(receiverId);
        request.setAmount(amount);
        request.setCurrency(Currency.USD);

        Transfer transfer = new Transfer(UUID.randomUUID(), senderId, receiverId, amount, Currency.USD, null, LocalDateTime.now());
        TransferResponse response = new TransferResponse(transfer.getId(), senderId, receiverId, amount, Currency.USD.toString(), TransferStatus.COMPLETED, transfer.getCreatedAt());

        when(createTransferUseCase.createTransfer(senderId, receiverId, amount, Currency.USD)).thenReturn(transfer);
        when(transferDtoMapper.toResponse(transfer)).thenReturn(response);

        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.senderId").value(senderId.toString()))
                .andExpect(jsonPath("$.receiverId").value(receiverId.toString()))
                .andExpect(jsonPath("$.amount").value(100.0))

                .andExpect(jsonPath("$.currency").value("USD"));
    }
    @WithMockUser
    @Test
    void shouldReturn404_whenAccountNotFound() throws Exception {
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        TransferRequest request = new TransferRequest();
        request.setSenderId(senderId);
        request.setReceiverId(receiverId);
        request.setAmount(new BigDecimal("50.00"));
        request.setCurrency(Currency.USD);

        when(createTransferUseCase.createTransfer(any(), any(), any(), any()))
                .thenThrow(new AppException("Account not found", "ACCOUNT_NOT_FOUND", org.springframework.http.HttpStatus.NOT_FOUND, LocalDateTime.now()));

        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
    @WithMockUser
    @Test
    void shouldReturn400_whenInsufficientBalance() throws Exception {
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        TransferRequest request = new TransferRequest();
        request.setSenderId(senderId);
        request.setReceiverId(receiverId);
        request.setAmount(new BigDecimal("9999.00"));
        request.setCurrency(Currency.USD);

        when(createTransferUseCase.createTransfer(any(), any(), any(), any()))
                .thenThrow(new InsufficientBalanceException("Insufficient balance"));

        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
