package com.simulator.model;

import java.math.BigDecimal;

public record LoanRequest(
        Vehicle vehicle,
        BigDecimal totalLoanAmount,
        int tenorYears,
        BigDecimal downPayment
) {
}
