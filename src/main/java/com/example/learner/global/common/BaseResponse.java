package com.example.learner.global.common;

import com.example.learner.global.common.code.SuccessCode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"status", "message", "result"})
public class BaseResponse<T> {
    private T result;
    private int status;
    private String message;

    public static <T> ResponseEntity<BaseResponse<T>> success(SuccessCode successCode, T data) {
        return ResponseEntity
                .status(successCode.getStatus())
                .body(new BaseResponse<>(
                        data,
                        successCode.getStatus(),
                        successCode.getMessage()
                ));
    }
}
