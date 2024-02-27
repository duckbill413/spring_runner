package com.example.learner.global.security.dto;

import com.example.learner.global.validator.Token;

public record AuthTokenReq(
        @Token String refreshToken
) {
}
