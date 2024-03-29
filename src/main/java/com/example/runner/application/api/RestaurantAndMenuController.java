package com.example.runner.application.api;

import com.example.runner.application.dto.response.RestaurantAndMenuDetailRes;
import com.example.runner.application.usecase.GetRestaurantAndMenuUsecase;
import com.example.runner.global.common.BaseResponse;
import com.example.runner.global.common.code.SuccessCode;
import com.example.runner.global.security.user.UserSecurityDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "매장 Usecase", description = "매장과 매장의 메뉴 조회 usecase controller")
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants/menu")
public class RestaurantAndMenuController {
    private final GetRestaurantAndMenuUsecase getRestaurantAndMenuUsecase;

    @Operation(summary = "매장과 메뉴 조회")
    @GetMapping("")
    public ResponseEntity<BaseResponse<RestaurantAndMenuDetailRes>> getRestaurantAndMenuDetail(
            @RequestParam(value = "restaurant") UUID restaurantId,
            @AuthenticationPrincipal UserSecurityDTO userSecurityDTO
    ) {
        log.info("Authentication 요청한 유저 ID: {}", userSecurityDTO.getId());
        var result = getRestaurantAndMenuUsecase.getRestaurantAndMenuDetail(restaurantId);
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                result
        );
    }
}
