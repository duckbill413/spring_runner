package com.example.learner.domain.member.entity;

public enum MemberRole {
    ANONYMOUS("익명 사용자"),
    USER("유저"),
    MANAGER("매니저"),
    ADMIN("관리자"),
    ;

    final String korean;

    MemberRole(String korean) {
        this.korean = korean;
    }
}
