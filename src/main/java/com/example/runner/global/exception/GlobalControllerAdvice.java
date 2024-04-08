package com.example.runner.global.exception;

import com.example.runner.global.common.ErrorResponse;
import com.example.runner.global.common.code.ErrorCode;
import com.example.runner.global.security.exception.AccessTokenException;
import com.example.runner.global.security.exception.RefreshTokenException;
import com.example.runner.global.security.exception.TokenErrorResponse;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.io.IOException;
import java.util.Objects;

@Log4j2
@RestControllerAdvice
public class GlobalControllerAdvice {
    /**
     * Access token 확인 에러
     *
     * @param e AccessTokenException
     * @return error response body with status
     */
    @ExceptionHandler(AccessTokenException.class)
    public ResponseEntity<TokenErrorResponse> handleAccessTokenException(AccessTokenException e) {
        var response = TokenErrorResponse.builder()
                .status(e.getError().getStatus())
                .message(e.getError().getMessage())
                .build();
        return new ResponseEntity<>(response, e.getError().getStatus());
    }

    /**
     * Refresh token 확인 에러
     *
     * @param e RefreshTokenException
     * @return error response body with status
     */
    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<TokenErrorResponse> handleRefreshTokenException(RefreshTokenException e) {
        var response = TokenErrorResponse.builder()
                .status(e.getError().getStatus())
                .message(e.getError().getMessage())
                .build();
        return new ResponseEntity<>(response, e.getError().getStatus());
    }

    // Custom ErrorCode를 기반으로 에러 처리
    @ExceptionHandler(BaseExceptionHandler.class)
    public ResponseEntity<ErrorResponse> handleCustomBaseExceptionHandler(BaseExceptionHandler e) {
        ErrorResponse response = ErrorResponse.of()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodValidation(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        ErrorResponse response = ErrorResponse.of()
                .code(ErrorCode.NOT_VALID_ERROR)
                .message(ex.getMessage())
                .errors(Objects.requireNonNull(bindingResult).getFieldErrors())
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleHandlerMethodValidation(HandlerMethodValidationException ex) {
        ErrorResponse response = ErrorResponse.of()
                .code(ErrorCode.NOT_VALID_ERROR)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * 예외 처리 되지 않은 모든 에러 처리
     *
     * @param e Exception
     * @return ResponseEntity
     */
    @ExceptionHandler({Exception.class})
    protected ResponseEntity<ErrorResponse> handleAllExceptions(Exception e) {
        e.printStackTrace();
        ErrorResponse response = ErrorResponse.of()
                .code(ErrorCode.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
