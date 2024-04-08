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

@Getter
public class RefreshTokenException extends RuntimeException {
    private final REFRESH_TOKEN_ERROR error;

    public RefreshTokenException(REFRESH_TOKEN_ERROR error) {
        this.error = error;
    }
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
}
