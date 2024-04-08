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

public class RefreshTokenException extends TokenException {
    private final REFRESH_TOKEN_ERROR error;

    @Getter
    public enum REFRESH_TOKEN_ERROR {
        NO_ACCESS(HttpStatus.UNAUTHORIZED, "No access"),
        BAD_ACCESS(HttpStatus.UNAUTHORIZED, "Bad access"),
        NO_REFRESH(HttpStatus.UNAUTHORIZED, "No refresh token"),
        OLD_REFRESH(HttpStatus.FORBIDDEN, "Old refresh token"),
        BAD_REFRESH(HttpStatus.FORBIDDEN, "Bad refresh token"),
        ;
        private final HttpStatus status;
        private final String message;

        REFRESH_TOKEN_ERROR(HttpStatus status, String message) {
            this.status = status;
            this.message = message;
        }
    }

    public RefreshTokenException(REFRESH_TOKEN_ERROR error) {
        super(error.name());
        this.error = error;
    }

    public void addResponseError(HttpServletResponse response) throws IOException {
        super.addTokenErrorResponse(response, error.getStatus(), error.getMessage());
    }
}
