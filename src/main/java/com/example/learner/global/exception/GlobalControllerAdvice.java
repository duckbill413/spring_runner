package com.example.learner.global.exception;

import com.example.learner.global.common.ErrorResponse;
import com.example.learner.global.common.code.ErrorCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.Objects;

@Log4j2
@RestControllerAdvice
public class GlobalControllerAdvice {
    // Custom ErrorCode를 기반으로 에러 처리
    @ExceptionHandler(BaseExceptionHandler.class)
    public ResponseEntity<ErrorResponse> handleCustomBaseExceptionHandler(BaseExceptionHandler e) {
        ErrorResponse response = ErrorResponse.of()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodValidation(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        ErrorResponse response = ErrorResponse.of()
                .code(ErrorCode.NOT_VALID_ERROR)
                .message(ex.getMessage())
                .errors(Objects.requireNonNull(bindingResult).getFieldErrors())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleHandlerMethodValidation(HandlerMethodValidationException ex) {
        ErrorResponse response = ErrorResponse.of()
                .code(ErrorCode.NOT_VALID_ERROR)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleRuntimeExceptions(RuntimeException e) {
        e.printStackTrace();
        ErrorResponse response = ErrorResponse.of()
                .code(ErrorCode.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
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

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
