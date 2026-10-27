package com.simulator.factory;

import com.simulator.model.*;

public class VehicleFactory {
    private VehicleFactory() {}

    public static Vehicle createVehicle(String typeStr, String conditionStr, int year) {
        VehicleType type = VehicleType.fromString(typeStr);
        VehicleCondition condition = VehicleCondition.fromString(conditionStr);

        return switch (type) {
            case MOBIL -> new Car(condition, year);
            case MOTOR -> new Motorcycle(condition, year);
        };
    }
}
