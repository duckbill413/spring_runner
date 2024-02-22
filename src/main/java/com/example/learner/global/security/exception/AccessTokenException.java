package com.example.learner.global.security.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

/**
 * author        : duckbill413
 * date          : 2023-04-26
 * description   :
 **/

public class AccessTokenException extends TokenException {
    private final ACCESS_TOKEN_ERROR error;

    @Getter
    public enum ACCESS_TOKEN_ERROR {
        UN_ACCEPT(401, "Token is null or too short"),
        BAD_TYPE(401, "Token type Bearer"),
        MAL_FORM(403, "Malformed Token"),
        BAD_SIGN(403, "Bad Signatured Token"),
        EXPIRED(403, "Expired Token"),
        ;

        private final int status;
        private final String message;

        ACCESS_TOKEN_ERROR(int status, String message) {
            this.status = status;
            this.message = message;
        }

    }

    public AccessTokenException(ACCESS_TOKEN_ERROR error) {
        super(error.name());
        this.error = error;
    }

    public void addResponseError(HttpServletResponse response) {
        addTokenErrorResponse(response, error.getStatus(), error.getMessage());
    }
}
