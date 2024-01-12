package com.example.learner.learn.day1;

import lombok.Builder;

public record PersonRecord(String name,
                           int age) {
    public PersonRecord {
        if(name.isBlank()) {
            name = "홍길동";
        }
//        this.name = name;
    }
}
