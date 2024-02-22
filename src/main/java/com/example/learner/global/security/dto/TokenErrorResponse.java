package com.example.learner.global.security.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Schema(description = "Token Error Resposnsee")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonPropertyOrder({"code", "message", "time"})
public class TokenErrorResponse {
    private int code;
    private String message;
    private String time;
    @Builder
    public TokenErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        this.time = LocalDateTime.now().format(formatter);
    }
    public LocalDateTime getLocalDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return LocalDateTime.parse(this.time, formatter);
    }
    public LocalDate getLocalDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return LocalDate.parse(this.time, formatter);
    }
}
