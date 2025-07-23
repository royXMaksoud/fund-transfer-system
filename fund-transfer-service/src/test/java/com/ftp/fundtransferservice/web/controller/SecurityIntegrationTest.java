package com.ftp.fundtransferservice.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ✅ Integration Test: Unauthorized Access
 *
 * Verifies that protected endpoints require valid JWT token.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void shouldRejectRequestWithoutToken() throws Exception {
        mockMvc.perform(get("/accounts/0b3b9c60-0fa8-490a-9746-acffa946faa9") // Existing valid account ID
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()); // Expect 403 Forbidden
    }
}
