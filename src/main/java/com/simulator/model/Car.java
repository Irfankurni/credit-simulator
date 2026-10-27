package com.simulator.model;

import java.math.BigDecimal;

public class Car extends Vehicle {
    private static final BigDecimal BASE_RATE = new BigDecimal("8.0");

    public Car(VehicleCondition condition, int year) {
        super(VehicleType.MOTOR, condition, year);
    }

    @Override
    public BigDecimal getBaseInterestRate() {
        return BASE_RATE;
    }
}
