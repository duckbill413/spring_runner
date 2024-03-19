package com.example.runner.global.security.dto;

import com.example.runner.global.validator.Token;

public record AuthTokenReq(
        @Token String refreshToken
) {
}
