package com.example.runner.domain.member.entity;

public enum MemberRole {
    ANONYMOUS("익명 사용자"),
    BASIC_USER("최초 가입 유저"),
    USER("유저"),
    MANAGER("매니저"),
    ADMIN("관리자"),
    ;

    final String korean;

    MemberRole(String korean) {
        this.korean = korean;
    }
}
