package com.ftp.fundtransferservice;

import com.ftp.fundtransferservice.config.LockStrategyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(LockStrategyProperties.class)
public class FundTransferServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundTransferServiceApplication.class, args);
    }

}
