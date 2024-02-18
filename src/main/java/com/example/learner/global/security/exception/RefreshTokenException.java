package com.example.learner.global.security.exception;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.joda.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Map;

/**
 * author        : duckbill413
 * date          : 2023-04-26
 * description   :
 **/

public class RefreshTokenException extends Exception {
    private final ERROR_CASE error_case;

    @Getter
    public enum ERROR_CASE {
        NO_ACCESS(401, "No access"),
        BAD_ACCESS(401, "Bad access"),
        NO_REFRESH(403, "No Refresh Token"),
        OLD_REFRESH(403, "Old Refresh Token"),
        BAD_REFRESH(403, "Bad Refresh Token"),
        ;
        private final int status;
        private final String message;

        ERROR_CASE(int status, String message) {
            this.status = status;
            this.message = message;
        }
    }

    public RefreshTokenException(ERROR_CASE error_case) {
        super(error_case.name());
        this.error_case = error_case;
    }

    public void sendResponseError(HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String responseStr = gson.toJson(
                Map.of(
                        "code", error_case.getStatus(),
                        "message", error_case.getMessage(),
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
