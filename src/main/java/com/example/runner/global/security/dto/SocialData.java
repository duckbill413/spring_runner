package com.example.runner.global.security.dto;

import com.example.runner.domain.member.entity.SocialType;

public record SocialData(
        SocialType socialType,
        String id,
        String email,
        String phone
) {
}
