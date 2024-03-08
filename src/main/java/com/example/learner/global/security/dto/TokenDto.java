package com.example.learner.global.security.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TokenDto(
        String accessToken,
        String refreshToken,
        List<String> roles
) {
}