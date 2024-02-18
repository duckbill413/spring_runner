package com.example.learner.global.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenDto(
        @JsonProperty("access-token") String accessToken,
        @JsonProperty("refresh-token") String refreshToken
) {
}