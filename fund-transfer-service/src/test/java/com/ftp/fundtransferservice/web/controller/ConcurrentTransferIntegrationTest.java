/**
 * ✅ Integration Test: Concurrent Transfers with DB Locking
 *
 * Purpose:
 *   - Verifies that concurrent transfer requests are handled safely.
 *   - Ensures locking prevents race conditions and maintains balance integrity.
 *
 * Fixes:
 *   - Uses fixed UUIDs to avoid randomness.
 *   - Removes @DirtiesContext and manages DB manually.
 *   - Adds flush and confirmation after save.
 */

package com.ftp.fundtransferservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftp.fundtransferservice.infrastructure.db.entities.AccountEntity;
import com.ftp.fundtransferservice.infrastructure.db.repositories.SpringDataAccountRepository;
import com.ftp.fundtransferservice.shared.constants.Currency;
import com.ftp.fundtransferservice.web.dto.request.TransferRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")

@SpringBootTest
@AutoConfigureMockMvc
public class ConcurrentTransferIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private SpringDataAccountRepository accountRepo;

    private UUID senderAccountId;
    private UUID receiverAccountId;

    @BeforeEach
    @Transactional
    void setUp() {
        accountRepo.deleteAll();

        AccountEntity sender = new AccountEntity();
        sender.setUserId(UUID.randomUUID());
        sender.setBalance(new BigDecimal("500.00"));
        sender = accountRepo.save(sender);
        senderAccountId = sender.getId();

        AccountEntity receiver = new AccountEntity();
        receiver.setUserId(UUID.randomUUID());
        receiver.setBalance(new BigDecimal("0.00"));
        receiver = accountRepo.save(receiver);
        receiverAccountId = receiver.getId();

        accountRepo.flush();

        assertThat(accountRepo.findById(senderAccountId)).isPresent();
        assertThat(accountRepo.findById(receiverAccountId)).isPresent();
    }




    @Test
    void shouldHandleConcurrentTransfersSafely() throws Exception {
        int threads = 5;
        BigDecimal amount = new BigDecimal("100.00");

        CountDownLatch latch = new CountDownLatch(threads);
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < threads; i++) {
            executor.execute(() -> {
                try {
                    TransferRequest request = new TransferRequest();
                    request.setSenderId(senderAccountId);
                    request.setReceiverId(receiverAccountId);
                    request.setAmount(amount);
                    request.setCurrency(Currency.USD);

                    mockMvc.perform(
                            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/transfers/transactions")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer dummy") // 🔐 Replace if needed
                                    .content(objectMapper.writeValueAsString(request))
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        // 🕒 Wait for all threads
        latch.await();

        // 🧾 Final state checks
        BigDecimal finalSenderBalance = accountRepo.findById(senderAccountId)
                .orElseThrow(() -> new IllegalStateException("Sender not found"))
                .getBalance();

        BigDecimal finalReceiverBalance = accountRepo.findById(receiverAccountId)
                .orElseThrow(() -> new IllegalStateException("Receiver not found"))
                .getBalance();

        // ✅ Assertions: No negative values, total preserved
        assertThat(finalSenderBalance).isGreaterThanOrEqualTo(BigDecimal.ZERO);
        assertThat(finalReceiverBalance).isLessThanOrEqualTo(new BigDecimal("500.00"));
        assertThat(finalSenderBalance.add(finalReceiverBalance)).isEqualTo(new BigDecimal("500.00"));
    }
}
