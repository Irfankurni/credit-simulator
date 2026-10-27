package com.simulator.service;

import com.simulator.factory.VehicleFactory;
import com.simulator.model.InstallmentResult;
import com.simulator.model.LoanRequest;
import com.simulator.model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoanCalculationServiceTest {

    private LoanCalculationService calculationService;

    @BeforeEach
    void setUp() {
        calculationService = new LoanCalculationService();
    }

    @Test
    @DisplayName("Should match exact Excel formula calculation for 3-year Car loan")
    void shouldMatchExcelFormulaForThreeYearCarLoan() {
        Vehicle car = VehicleFactory.createVehicle("Mobil", "Bekas", 2022);
        LoanRequest request = new LoanRequest(
                car,
                new BigDecimal("100000000"),
                3,
                new BigDecimal("25000000")
        );

        List<InstallmentResult> results = calculationService.calculateInstallments(request);

        assertEquals(3, results.size());

        // Tahun 1: Rp 2,250,000.00/bln, Suku Bunga 8.0%
        assertEquals(1, results.getFirst().year());
        assertEquals(new BigDecimal("2250000.00"), results.get(0).monthlyInstallment());
        assertEquals(new BigDecimal("8.0"), results.get(0).interestRatePercent());

        // Tahun 2: Rp 2,432,250.00/bln, Suku Bunga 8.1%
        assertEquals(2, results.get(1).year());
        assertEquals(new BigDecimal("2432250.00"), results.get(1).monthlyInstallment());
        assertEquals(new BigDecimal("8.1"), results.get(1).interestRatePercent());

        // Tahun 3: Rp 2,641,423.50/bln, Suku Bunga 8.6%
        assertEquals(3, results.get(2).year());
        assertEquals(new BigDecimal("2641423.50"), results.get(2).monthlyInstallment());
        assertEquals(new BigDecimal("8.6"), results.get(2).interestRatePercent());
    }

    @Test
    @DisplayName("Should calculate progressive interest rate for Motorcycle up to 6 years")
    void shouldCalculateProgressiveInterestRatesForMotorcycle() {
        Vehicle motor = VehicleFactory.createVehicle("Motor", "Baru", 2026);
        LoanRequest request = new LoanRequest(
                motor,
                new BigDecimal("20000000"),
                6,
                new BigDecimal("7000000")
        );

        List<InstallmentResult> results = calculationService.calculateInstallments(request);

        assertEquals(6, results.size());
        assertEquals(new BigDecimal("9.0"), results.get(0).interestRatePercent());
        assertEquals(new BigDecimal("9.1"), results.get(1).interestRatePercent());
        assertEquals(new BigDecimal("9.6"), results.get(2).interestRatePercent());
        assertEquals(new BigDecimal("9.7"), results.get(3).interestRatePercent());
        assertEquals(new BigDecimal("10.2"), results.get(4).interestRatePercent());
        assertEquals(new BigDecimal("10.3"), results.get(5).interestRatePercent());
    }
}