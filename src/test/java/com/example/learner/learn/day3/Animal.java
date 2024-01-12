package com.example.learner.learn.day3;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
public class Animal {
    private String name;
    private int age;

    @Builder
    public Animal(String name, int age) {
        this.name = Objects.nonNull(name) ? name : "초코";
        this.age = age <= 0 ? 1 : age;
    }

    @Builder(builderMethodName = "fromAge", buildMethodName = "newAnimal")
    public Animal(int age) {
        this.name = "new!!!";
        this.age = age;
    }
}
