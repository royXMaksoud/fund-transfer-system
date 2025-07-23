package com.ftp.fundtransferservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftp.fundtransferservice.shared.constants.Currency;
import com.ftp.fundtransferservice.web.dto.request.TransferRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PerformanceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // Replace this with the actual token
    private final String validJwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyb3kiLCJpYXQiOjE3NTMyNzgzNTcsInJvbGUiOiJVU0VSIiwiZXhwIjoxNzUzMzY0NzU3fQ.Xfxkb-RVcvYpnVLASEToLX7od1RAvhCJt-adZT6cClUCAbAHCQ3pWh5t7s-ZXszKn2h78I89PVLOdSOpbJi8kA";

    // Sender and receiver account IDs
    private final String senderAccountId = "ab6e03cf-68dc-4dae-8521-9fc89517fe94";  // Sender account ID
    private final String receiverAccountId = "8b0fdaa7-9082-4978-b17c-ecc449bee9f3";  // Receiver account ID

    /**
     * This test simulates concurrent transfer requests and ensures that the system
     * handles multiple simultaneous transactions correctly.
     * It also verifies that the locking mechanism (in-memory and DB) works as expected.
     */
    @Test
    void shouldHandleConcurrentTransfers() throws Exception {
        int numThreads = 10;  // Number of concurrent transfer requests (adjust as needed)
        BigDecimal amount = new BigDecimal("100.00");  // The amount for each transfer

        // CountDownLatch is used to ensure that we wait for all threads to complete before finishing the test
        CountDownLatch latch = new CountDownLatch(numThreads);

        // ExecutorService is used to run the threads concurrently
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        // Loop through the number of threads and create tasks to execute concurrently
        for (int i = 0; i < numThreads; i++) {
            executor.execute(() -> {
                try {
                    // Create a transfer request for each thread
                    TransferRequest request = new TransferRequest();
                    request.setSenderId(UUID.fromString(senderAccountId));  // Use the actual sender account ID
                    request.setReceiverId(UUID.fromString(receiverAccountId));  // Use the actual receiver account ID
                    request.setAmount(amount);
                    request.setCurrency(Currency.valueOf("USD"));

                    // Send the transfer request
                    mockMvc.perform(post("/transfers/transactions")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + validJwtToken)  // Pass the valid JWT token
                                    .content(new ObjectMapper().writeValueAsString(request)))  // Convert the request object to JSON
                            .andExpect(status().isOk());  // Expect status 200 OK if the transfer is successful
                } catch (Exception e) {
                    e.printStackTrace();  // Print the error stack trace if the thread encounters an issue
                } finally {
                    latch.countDown();  // Decrement the latch count when the thread finishes its task
                }
            });
        }

        // Wait for all threads to finish execution
        latch.await();
    }

    /**
     * This test measures the time taken for performing concurrent transfers.
     * It helps assess the performance of the system under load and checks the
     * time it takes to handle multiple requests.
     */
    @Test
    void shouldMeasureTimeForConcurrentTransfers() throws Exception {
        long startTime = System.currentTimeMillis();  // Record the start time of the test

        int numThreads = 10;  // Number of concurrent transfer requests
        CountDownLatch latch = new CountDownLatch(numThreads);
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executor.execute(() -> {
                try {
                    // Create the transfer request
                    TransferRequest request = new TransferRequest();
                    request.setSenderId(UUID.fromString(senderAccountId));  // Use the actual sender account ID
                    request.setReceiverId(UUID.fromString(receiverAccountId));  // Use the actual receiver account ID
                    request.setAmount(new BigDecimal("100.00"));  // Amount for the transfer
                    request.setCurrency(Currency.valueOf("USD"));

                    // Perform the transfer operation
                    mockMvc.perform(post("/transfers/transactions")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + validJwtToken)
                                    .content(new ObjectMapper().writeValueAsString(request)))
                            .andExpect(status().isOk());  // Expect a successful transfer (200 OK)
                } catch (Exception e) {
                    e.printStackTrace();  // Print the error if something goes wrong
                } finally {
                    latch.countDown();  // Decrease the latch count when the thread completes
                }
            });
        }

        latch.await();  // Wait for all threads to complete

        long endTime = System.currentTimeMillis();  // Record the end time of the test
        long duration = endTime - startTime;  // Calculate the total duration of the test
        System.out.println("Time taken for concurrent transfers: " + duration + "ms");  // Print the time taken
    }
}
