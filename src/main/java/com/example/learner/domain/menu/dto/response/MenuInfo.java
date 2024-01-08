/*
 * author : duckbill413
 * created date : 2024-01-07
 * updated date : 2024-01-07
 * description : 메뉴 정보 DTO
 */

package com.example.learner.domain.menu.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "메뉴 정보")
public record MenuInfo(
        @Schema(description = "메뉴 Id")
        Long id,
        @Schema(description = "메뉴 이름")
        String name,
        @Schema(description = "메뉴 가격")
        Long price,
        @Schema(description = "메뉴 재고 개수")
        Long stock
) {
}
