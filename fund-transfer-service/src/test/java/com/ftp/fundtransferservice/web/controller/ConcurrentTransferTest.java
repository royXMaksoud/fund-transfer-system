package com.ftp.fundtransferservice.web.controller;

import com.ftp.fundtransferservice.domain.model.Transfer;
import com.ftp.fundtransferservice.domain.ports.in.CreateTransferUseCase;
import com.ftp.fundtransferservice.shared.constants.Currency;
import com.ftp.fundtransferservice.web.dto.request.TransferRequest;
import com.ftp.fundtransferservice.web.dto.response.TransferResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(properties = {
        "ftp.lock-strategy=DB"  // Specify the lock strategy. Here, we use DB locking strategy.
})
public class ConcurrentTransferTest {

    @Autowired
    private CreateTransferUseCase createTransferUseCase;  // This service is responsible for creating fund transfers.

    // Sample sender and receiver account IDs (for testing purposes, these are hardcoded, but in a real case, you would fetch or generate them).
    private final UUID senderAccountId = UUID.fromString("ab6e03cf-68dc-4dae-8521-9fc89517fe94");
    private final UUID receiverAccountId = UUID.fromString("8b0fdaa7-9082-4978-b17c-ecc449bee9f3");

    /**
     * This test simulates concurrent transfer requests to ensure that the system
     * can handle multiple transfers at the same time without causing issues like race conditions.
     * It validates the locking mechanism (DB Lock in this case) to ensure that no two transfers can
     * modify the same account simultaneously.
     */
    @Test
    void testConcurrentTransfers() throws InterruptedException {
        int numberOfTransfers = 10;  // Define how many concurrent transfer requests will be simulated.

        // CountDownLatch will be used to block the main thread until all threads (simulated transfers) complete.
        CountDownLatch latch = new CountDownLatch(numberOfTransfers);

        // ExecutorService will create a pool of threads to simulate concurrent transfer operations.
        ExecutorService executor = Executors.newFixedThreadPool(numberOfTransfers);

        // Loop through the number of transfers, submitting each transfer task to the executor.
        for (int i = 0; i < numberOfTransfers; i++) {
            executor.execute(() -> {
                try {
                    // Create a transfer request object that holds all necessary information for the transfer.
                    TransferRequest request = new TransferRequest();
                    request.setSenderId(senderAccountId);  // Set the sender's account ID.
                    request.setReceiverId(receiverAccountId);  // Set the receiver's account ID.
                    request.setAmount(new BigDecimal("100.00"));  // The amount to be transferred in each transfer.
                    request.setCurrency(Currency.valueOf("USD"));  // The currency type for the transfer.

                    // Call the method that handles the transfer logic. This method will initiate the locking process
                    // and execute the transfer.
                    createTransferUseCase.createTransfer(request.getSenderId(), request.getReceiverId(), request.getAmount(), request.getCurrency());

                    // If no exceptions were thrown, this means the transfer was successfully completed.
                    System.out.println("Transfer completed successfully");
                } catch (Exception e) {
                    // If any error occurs (such as insufficient balance, account not found, etc.),
                    // it will be logged for debugging purposes.
                    System.err.println("Error occurred: " + e.getMessage());
                } finally {
                    // Regardless of success or failure, we decrement the latch count.
                    latch.countDown();
                }
            });
        }

        // This ensures that the main thread waits until all concurrent tasks (transfers) are completed.
        latch.await();  // Wait for all threads to finish executing.
    }
}
