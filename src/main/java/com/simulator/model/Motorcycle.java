package com.simulator.model;

import java.math.BigDecimal;

public class Motorcycle extends Vehicle {
    private static final BigDecimal BASE_RATE = new BigDecimal("9.0");

    public Motorcycle(VehicleCondition condition, int year) {
        super(VehicleType.MOTOR, condition, year);
    }

    @Override
    public BigDecimal getBaseInterestRate() {
        return BASE_RATE;
    }
}
