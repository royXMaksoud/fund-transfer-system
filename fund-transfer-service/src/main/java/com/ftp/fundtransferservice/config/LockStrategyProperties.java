package com.ftp.fundtransferservice.config;

import com.ftp.fundtransferservice.shared.constants.LockStrategyType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties to hold the lock strategy configuration.
 * <p>
 * The properties are prefixed with <code>ftp</code> in application configuration files.
 * Example usage in <code>application.yml</code>:
 * <pre>
 * ftp:
 *   lockStrategy: MEMORY
 * </pre>
 * or
 * <pre>
 * ftp:
 *   lockStrategy: DB
 * </pre>
 * <p>
 * This property controls which locking mechanism to use for fund transfers.
 * The value of this property determines if the system should use memory-based locking
 * or database-based locking for fund transfer operations.
 */
@ConfigurationProperties(prefix = "ftp") // Binds properties with prefix "ftp" to this class
public class LockStrategyProperties {

    /**
     * The lock strategy type.
     * This property determines the type of locking mechanism to use for fund transfer operations.
     * Possible values are MEMORY or DB.
     */
    private LockStrategyType lockStrategy;

    /**
     * Gets the configured lock strategy.
     *
     * @return the current lock strategy
     */
    public LockStrategyType getLockStrategy() {
        return lockStrategy; // Return the configured lock strategy
    }

    /**
     * Sets the lock strategy.
     * This method allows updating the lock strategy.
     *
     * @param lockStrategy the lock strategy to set
     */
    public void setLockStrategy(LockStrategyType lockStrategy) {
        this.lockStrategy = lockStrategy; // Set the lock strategy to the provided value
    }
}
