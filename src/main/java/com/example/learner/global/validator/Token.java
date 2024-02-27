package com.example.learner.global.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TokenValidator.class)
public @interface Token {
    String message() default "Invalid Refresh Token";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
