package com.simulator.service;

import com.simulator.model.InstallmentResult;
import com.simulator.model.LoanRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class LoanCalculationService {
    private static final BigDecimal ONE_YEAR_INCREMENT = new BigDecimal("0.1");
    private static final BigDecimal TWO_YEAR_INCREMENT = new BigDecimal("0.5");
    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final BigDecimal MONTHS_IN_YEAR = new BigDecimal("12");

    public List<InstallmentResult> calculateInstallments(LoanRequest request) {
        List<InstallmentResult> results = new ArrayList<>();

        BigDecimal currentPrincipal = request.totalLoanAmount().subtract(request.downPayment());
        BigDecimal currentRatePercent = request.vehicle().getBaseInterestRate();
        int tenorYears = request.tenorYears();

        for (int year = 0; year <= tenorYears; year++) {
            if (year > 1) {
                if (year % 2 == 0) {
                    currentRatePercent = currentRatePercent.add(ONE_YEAR_INCREMENT);
                } else {
                    currentRatePercent = currentRatePercent.add(TWO_YEAR_INCREMENT);
                }
            }

            BigDecimal rateDecimal = currentRatePercent.divide(HUNDRED, 8, RoundingMode.HALF_UP);
            BigDecimal totalLoanWithInterest = currentPrincipal.add(currentPrincipal.multiply(rateDecimal));

            int remainingMonths = (12 * tenorYears) - (12 * (year - 1));
            BigDecimal monthlyInstallment = totalLoanWithInterest.divide(
                    new BigDecimal(remainingMonths), 2, RoundingMode.HALF_UP);

            BigDecimal yearlyInstallment = monthlyInstallment.multiply(MONTHS_IN_YEAR);
            results.add(new InstallmentResult(year, monthlyInstallment, currentRatePercent));
            currentPrincipal = totalLoanWithInterest.subtract(yearlyInstallment);
        }

        return results;
    }
}
