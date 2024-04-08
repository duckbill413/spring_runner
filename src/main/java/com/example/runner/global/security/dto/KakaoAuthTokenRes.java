package com.example.runner.global.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoAuthTokenRes(
        Long id,
        @JsonProperty(value = "kakao_account")
        KakaoAccount kakaoAccount
) {
}
