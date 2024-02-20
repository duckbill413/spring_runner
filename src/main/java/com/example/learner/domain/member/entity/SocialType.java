package com.example.learner.domain.member.entity;

import com.example.learner.global.common.code.ErrorCode;
import com.example.learner.global.exception.BaseExceptionHandler;

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
