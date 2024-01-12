package com.example.learner.global.exception;

import com.example.learner.global.common.code.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BaseExceptionHandler extends RuntimeException {
    private final ErrorCode errorCode;

    public BaseExceptionHandler(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public BaseExceptionHandler(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
