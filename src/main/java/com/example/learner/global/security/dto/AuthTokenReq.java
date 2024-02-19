package com.example.learner.global.security.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AuthTokenReq(
        @NotNull @NotEmpty String refreshToken
) {
}
