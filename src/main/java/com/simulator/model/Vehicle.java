package com.simulator.model;

import java.math.BigDecimal;

public abstract class Vehicle {

    private final VehicleType type;
    private final VehicleCondition condition;
    private final int year;

    protected Vehicle(VehicleType type, VehicleCondition condition, int year) {
        this.type = type;
        this.condition = condition;
        this.year = year;
    }

    public abstract BigDecimal getBaseInterestRate();

    public VehicleType getType() {
        return type;
    }

    public VehicleCondition getCondition() {
        return condition;
    }

    public int getYear() {
        return year;
    }
}
