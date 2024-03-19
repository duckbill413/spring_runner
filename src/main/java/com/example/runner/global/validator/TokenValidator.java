package com.example.runner.global.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TokenValidator implements ConstraintValidator<Token, String> {
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 토큰값이 null인 경우
        if (Objects.isNull(value)) {
            return false;
        }
        // 토큰이 header와 body로 분리되지 않은 경우
        String[] separatedToken = value.split(" ");
        if (separatedToken.length != 2) {
            return false;
        }

        return separatedToken[1].equalsIgnoreCase(TOKEN_PREFIX);
    }
}
