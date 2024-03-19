package com.example.runner.domain.restaurant.entity;

public enum Category {
    KOREAN("한식"),
    CHINESE("중식"),
    JAPANESE("일식"),
    WESTERN("양식"),
    ;
    final String korean;

    Category(String korean) {
        this.korean = korean;
    }
}
