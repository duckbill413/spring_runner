package com.example.runner.domain.member.entity;

public enum Gender {
    MAN("남성"), WOMAN("여성"),;
    final String korean;

    Gender(String korean) {
        this.korean = korean;
    }
}
