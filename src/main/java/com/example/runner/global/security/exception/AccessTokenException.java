package com.example.runner.global.security.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * author        : duckbill413
 * date          : 2023-04-26
 * description   :
 **/

public class AccessTokenException extends TokenException {
    private final ACCESS_TOKEN_ERROR error;

    @Getter
    public enum ACCESS_TOKEN_ERROR {
        UN_ACCEPT(HttpStatus.UNAUTHORIZED, "Token is null or too short"),
        BAD_TYPE(HttpStatus.UNAUTHORIZED, "Token type Bearer"),
        MAL_FORM(HttpStatus.FORBIDDEN, "Malformed token"),
        BAD_SIGN(HttpStatus.FORBIDDEN, "Bad signature token"),
        EXPIRED(HttpStatus.FORBIDDEN, "Expired token"),
        ;

        private final HttpStatus status;
        private final String message;

        ACCESS_TOKEN_ERROR(HttpStatus status, String message) {
            this.status = status;
            this.message = message;
        }

    }

    public AccessTokenException(ACCESS_TOKEN_ERROR error) {
        super(error.name());
        this.error = error;
    }

    public void addResponseError(HttpServletResponse response) throws IOException {
        super.addTokenErrorResponse(response, error.getStatus(), error.getMessage());
    }
}
