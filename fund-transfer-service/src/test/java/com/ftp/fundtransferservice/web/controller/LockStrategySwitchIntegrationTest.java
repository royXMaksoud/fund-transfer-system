package com.ftp.fundtransferservice.web.controller;

import com.ftp.fundtransferservice.application.service.locking.DbLockTransferService;
import com.ftp.fundtransferservice.application.service.locking.ReentrantLockTransferService;
import com.ftp.fundtransferservice.domain.ports.out.LockTransferPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ✅ Lock Strategy Integration Tests
 *
 * This test verifies that the correct locking strategy is used
 * based on the configuration property `ftp.lock-strategy`.
 *
 * Two strategies are supported:
 * - MEMORY → uses ReentrantLockTransferService (in-memory lock)
 * - DB     → uses DbLockTransferService (database-level lock)
 *
 * These tests ensure that the Spring context injects the correct
 * LockTransferPort implementation based on the selected strategy.
 */

// 🧪 Test when MEMORY lock strategy is selected
@SpringBootTest
@TestPropertySource(properties = {
        "ftp.lock-strategy=MEMORY"
})
class LockStrategyMemoryIntegrationTest {

    @Autowired
    private LockTransferPort lockTransferPort;

    @Test
    void whenMemoryStrategySelected_thenUseReentrantLockTransferService() {
        // ✅ Expect in-memory locking implementation
        assertThat(lockTransferPort)
                .isInstanceOf(ReentrantLockTransferService.class);
    }
}

// 🧪 Test when DB lock strategy is selected
@SpringBootTest
@TestPropertySource(properties = {
        "ftp.lock-strategy=DB"
})
class LockStrategyDbIntegrationTest {

    @Autowired
    private LockTransferPort lockTransferPort;

    @Test
    void whenDbStrategySelected_thenUseDbLockTransferService() {
        // ✅ Expect DB locking implementation
        assertThat(lockTransferPort)
                .isInstanceOf(DbLockTransferService.class);
    }
}
