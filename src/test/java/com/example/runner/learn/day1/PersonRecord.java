package com.example.runner.learn.day1;

public record PersonRecord(String name,
                           int age) {
    public PersonRecord {
        if(name.isBlank()) {
            name = "홍길동";
        }
//        this.name = name;
    }
}
