package com.example.learner.global.security.dto;

import com.example.learner.global.security.filter.Token;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AuthTokenReq(
        @Token String refreshToken
) {
}
