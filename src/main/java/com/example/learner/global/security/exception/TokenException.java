package com.example.learner.global.security.exception;

import com.example.learner.global.security.dto.TokenErrorResponse;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;

public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }

    protected static void addTokenErrorResponse(HttpServletResponse response, int status, String message) {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        var errorResponse = TokenErrorResponse.builder()
                .code(status)
                .message(message)
                .build();

        Gson gson = new Gson();
        try {
            response.getWriter().println(gson.toJson(errorResponse));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
