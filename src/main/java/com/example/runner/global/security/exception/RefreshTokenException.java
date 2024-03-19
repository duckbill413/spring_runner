package com.example.runner.global.security.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

/**
 * author        : duckbill413
 * date          : 2023-04-26
 * description   :
 **/

public class RefreshTokenException extends TokenException {
    private final REFRESH_TOKEN_ERROR error;

    @Getter
    public enum REFRESH_TOKEN_ERROR {
        NO_ACCESS(401, "No access"),
        BAD_ACCESS(401, "Bad access"),
        NO_REFRESH(403, "No Refresh Token"),
        OLD_REFRESH(403, "Old Refresh Token"),
        BAD_REFRESH(403, "Bad Refresh Token"),
        ;
        private final int status;
        private final String message;

        REFRESH_TOKEN_ERROR(int status, String message) {
            this.status = status;
            this.message = message;
        }
    }

    public RefreshTokenException(REFRESH_TOKEN_ERROR error) {
        super(error.name());
        this.error = error;
    }

    public void addResponseError(HttpServletResponse response) {
        addTokenErrorResponse(response, error.getStatus(), error.getMessage());
    }
}
