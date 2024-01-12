package com.example.learner.domain.member.entity;

public enum MemberRole {
    USER("유저"),
    MANAGER("매니저"),
    ADMIN("관리자"),
    ;

    final String korean;

    MemberRole(String korean) {
        this.korean = korean;
    }
}
