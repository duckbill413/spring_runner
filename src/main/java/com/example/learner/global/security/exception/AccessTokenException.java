package com.example.learner.global.security.exception;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.joda.time.LocalDateTime;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Map;

/**
 * author        : duckbill413
 * date          : 2023-04-26
 * description   :
 **/

public class AccessTokenException extends RuntimeException {
    private final TOKEN_ERROR token_error;

    @Getter
    public enum TOKEN_ERROR {
        UNACCEPT(401, "Token is null or too short"),
        BADTYPE(401, "Token type Bearer"),
        MALFORM(403, "Malformed Token"),
        BADSIGN(403, "Bad Signatured Token"),
        EXPIRED(403, "Expired Token");

        private final int status;
        private final String message;

        TOKEN_ERROR(int status, String message) {
            this.status = status;
            this.message = message;
        }

    }

    public AccessTokenException(TOKEN_ERROR error) {
        super(error.name());
        this.token_error = error;
    }

    public void sendResponseError(HttpServletResponse response) {
        response.setStatus(token_error.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();
        String responseStr = gson.toJson(
                Map.of(
                        "code", token_error.getStatus(),
                        "message", token_error.getMessage(),
                        "time", new LocalDateTime()
                )
        );
        try {
            response.getWriter().println(responseStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
