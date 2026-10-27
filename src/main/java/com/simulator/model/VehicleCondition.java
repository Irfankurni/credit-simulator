package com.simulator.model;

import com.simulator.exception.ValidationException;

import java.math.BigDecimal;

public enum VehicleCondition {
    BARU(new BigDecimal("0.35")),
    BEKAS(new BigDecimal("0.25"));

    private final BigDecimal minDpPercentage;

    VehicleCondition(BigDecimal minDpPercentage) {
        this.minDpPercentage = minDpPercentage;
    }

    public BigDecimal getMinDpPercentage() {
        return minDpPercentage;
    }

    public static VehicleCondition fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Kondisi Kendaraan tidak boleh kosong.");
        }

        try {
            return VehicleCondition.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Kondisi Kendaaraan harus 'Baru' atau 'Bekas'. Input: " + value);
        }
    }
}
