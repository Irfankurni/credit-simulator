package com.simulator.model;

import com.simulator.exception.ValidationException;

public enum VehicleType {
    MOBIL,MOTOR;

    public static VehicleType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Jenis Kendaraan tidak boleh kosong.");
        }
        try {
            return VehicleType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw  new ValidationException("Jenis Kendaraan harus 'Motor' atau 'Mobil'. Input: " + value);
        }
    }
}
