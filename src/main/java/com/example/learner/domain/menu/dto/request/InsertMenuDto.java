package com.example.learner.domain.menu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InsertMenuDto(
        @NotBlank(message = "메뉴명을 입력해주세요.") String name,
        @NotNull(message = "메뉴의 가격 정보를 입력해주세요.") Long price
) {
}
