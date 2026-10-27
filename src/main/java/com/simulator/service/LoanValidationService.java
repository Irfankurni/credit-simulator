package com.simulator.service;

import com.simulator.exception.ValidationException;
import com.simulator.model.VehicleCondition;

import java.math.BigDecimal;
import java.time.Year;

public class LoanValidationService {
    private static final BigDecimal MAX_LOAN_AMOUNT = new BigDecimal("1000000000");

    public int validateAndParseYear(String yearStr, VehicleCondition condition) {
        return validateAndParseYear(yearStr, condition, Year.now().getValue());
    }

    public int validateAndParseYear(String yearStr, VehicleCondition condition, int currentYear) {
        if (yearStr == null || !yearStr.trim().matches("\\d{4}$")) {
            throw new ValidationException("Tahun kendaraan harus berupa angka 4 digit.");
        }
        int year = Integer.parseInt(yearStr.trim());

        if (condition == VehicleCondition.BARU && year < (currentYear - 1)) {
            throw new ValidationException(String.format("Kendaraan kondisi BARU tidak boleh memiliki tahun kurang dari %d (Tahun sekarang - 1).",
                    currentYear - 1));
        }
        return year;
    }

    public BigDecimal validateAndParseLoanAmount(String amountStr) {
        BigDecimal amount = parsePositiveNumber(amountStr, "Jumlah Pinjaman Total");
        if (amount.compareTo(MAX_LOAN_AMOUNT) > 0) {
            throw new ValidationException("Jumlah Pinjaman Total tidak boleh lebih dari Rp 1.000.000.000 (1 Miliar).");
        }
        return amount;
    }

    public int validateAndParseTenor(String tenorStr) {
        if (tenorStr == null) {
            throw new ValidationException("Tenor pinjaman harus berupa angka bulat (1-6).");
        }
        try {
            int tenor = Integer.parseInt(tenorStr.trim());
            if (tenor < 1 || tenor > 6) {
                throw new ValidationException("Tenor pinjaman harus antara 1 sampai 6 tahun");
            }
            return tenor;
        } catch (NumberFormatException e) {
            throw new ValidationException("Tenor pinjaman harus berupa angka bulat (1-6).");
        }
    }

    public BigDecimal validateAndParseDownPayment(String dpStr, BigDecimal totalLoan, VehicleCondition condition) {
        BigDecimal dp = parsePositiveNumber(dpStr, "Jumlah DP");
        BigDecimal minDp = totalLoan.multiply(condition.getMinDpPercentage());

        if (dp.compareTo(minDp) < 0) {
            int minPercent = condition.getMinDpPercentage().multiply(new BigDecimal("100")).intValue();
            throw new ValidationException(String.format(
                    "Jumlah DP untuk kendaraan %s minimal %d%% dari Jumlah Pinjaman (Minimal Rp %,.2f).",
                    condition.name(), minPercent, minDp));
        }
        if (dp.compareTo(totalLoan) >= 0) {
            throw new ValidationException("Jumlah DP tidak boleh lebih besar atau sama dengan Jumlah Pinjaman Total.");
        }
        return dp;
    }

    private BigDecimal parsePositiveNumber(String val, String fieldName) {
        try {
            BigDecimal number = new BigDecimal(val.trim());
            if (number.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException(fieldName + " harus lebih besar dari 0.");
            }
            return number;
        } catch (NumberFormatException | NullPointerException e) {
            throw new ValidationException(fieldName + " harus berupa angka yang valid.");
        }
    }
}
