package com.example.runner.global.security.exception;

import com.example.runner.global.security.dto.TokenErrorResponse;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }

    protected void addTokenErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        response.getWriter().println(gson.toJson(TokenErrorResponse.builder()
                .code(status.value())
                .message(message)
                .build()));
    }
}
