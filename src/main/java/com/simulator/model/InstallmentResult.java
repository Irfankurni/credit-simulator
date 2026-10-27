package com.simulator.model;

import java.math.BigDecimal;

public record InstallmentResult(
        int year,
        BigDecimal monthlyInstallment,
        BigDecimal interestRatePercent
) {
}
