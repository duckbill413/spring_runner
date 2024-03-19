package com.example.runner.learn.day3;

import lombok.AccessLevel;
import lombok.Builder;

public class Vehicle {
    private final String name;
    private final int number;
    @Builder(access = AccessLevel.PRIVATE)
    private Vehicle(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public static Vehicle getVehicle(String name, int number) {
        return Vehicle.builder()
                .name(name)
                .number(number)
                .build();
    }
}
