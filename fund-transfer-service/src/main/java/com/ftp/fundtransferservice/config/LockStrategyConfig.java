package com.ftp.fundtransferservice.config;


import com.ftp.fundtransferservice.application.service.locking.DbLockTransferService;
import com.ftp.fundtransferservice.application.service.locking.ReentrantLockTransferService;
import com.ftp.fundtransferservice.domain.ports.out.LockTransferPort;
import com.ftp.fundtransferservice.shared.constants.LockStrategyType;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Configuration class to provide the correct {@link LockTransferPort} implementation
 * based on the configured lock strategy.
 * <p>
 * Supports two strategies:
 * <ul>
 *   <li>MEMORY: Uses in-memory ReentrantLock-based locking</li>
 *   <li>DB: Uses database-level locking</li>
 * </ul>
 * <p>
 * The active strategy is controlled via {@link LockStrategyProperties#getLockStrategy()}.
 */
@Configuration
@EnableConfigurationProperties(LockStrategyProperties.class)  //
public class LockStrategyConfig {

    private final LockStrategyProperties properties;
    /**
     * Constructs the configuration with injected lock strategy properties.
     *
     * @param properties configuration properties holding the lock strategy setting
     */
    public LockStrategyConfig(LockStrategyProperties properties) {
        this.properties = properties;
    }
    /**
     * Creates a {@link LockTransferPort} bean according to the configured lock strategy.
     *
     * @param memoryLockService in-memory lock implementation
     * @param dbLockService     database-level lock implementation
     * @return the appropriate {@link LockTransferPort} implementation
     * @throws UnsupportedOperationException if the configured lock strategy is not supported
     */
    @Bean
    public LockTransferPort lockTransferPort(
            ReentrantLockTransferService memoryLockService,
            DbLockTransferService dbLockService) {

        if (properties.getLockStrategy() == LockStrategyType.MEMORY) {
            return memoryLockService;
        } else if (properties.getLockStrategy() == LockStrategyType.DB) {
            return dbLockService;
        }

        throw new UnsupportedOperationException("Unsupported lock strategy: " + properties.getLockStrategy());
    }
}
