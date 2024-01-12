package com.example.learner.learn.day3;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

@Log4j2
class AnimalTest {
    @Test
    public void animalBuilderTest1() {
        Animal dog = Animal.builder()
                .name(null)
                .age(3)
                .build();
        log.info(dog);
    }

    @Test
    public void animalBuilderTest2() {
        Animal newAnimal = Animal.fromAge()
                .age(5)
                .newAnimal();
        log.info(newAnimal);
    }
}