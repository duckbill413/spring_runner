package com.example.runner.domain.member.entity;

import com.example.runner.global.common.code.ErrorCode;
import com.example.runner.global.exception.BaseExceptionHandler;

public enum SocialType {
    KAKAO, NAVER, GOOGLE;
    public static SocialType fromString(String value) {
        for (SocialType socialType : SocialType.values()) {
            if (socialType.name().equalsIgnoreCase(value)) {
                return socialType;
            }
        }

        throw new BaseExceptionHandler(ErrorCode.UNSUPPORTED_SOCIAL_PLATFORM);
    }
}
