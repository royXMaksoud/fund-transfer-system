package com.ftp.fundtransferservice.shared.constants;

import java.math.BigDecimal;

public class TransferConstants {

    public static final BigDecimal MAX_TRANSFER_AMOUNT = new BigDecimal("10000.00");
    public static final BigDecimal MIN_TRANSFER_AMOUNT = new BigDecimal("1.00");

    private TransferConstants() {
        // Prevent instantiation
    }
}
