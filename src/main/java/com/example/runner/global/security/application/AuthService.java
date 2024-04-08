package com.example.runner.global.security.application;

import com.example.runner.global.security.dto.KakaoAuthTokenRes;

public interface AuthService {
    KakaoAuthTokenRes verifyKakaoToken(String accessToken);
}