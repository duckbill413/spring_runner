package com.example.learner.application.api;

import com.example.learner.application.dto.response.RestaurantAndMenuDetailRes;
import com.example.learner.application.usecase.GetRestaurantAndMenuUsecase;
import com.example.learner.global.common.BaseResponse;
import com.example.learner.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "매장 Usecase", description = "매장과 매장의 메뉴 조회 usecase controller")
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurants/menu")
public class RestaurantAndMenuController {
    private final GetRestaurantAndMenuUsecase getRestaurantAndMenuUsecase;

    @Operation(summary = "매장과 메뉴 조회")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<RestaurantAndMenuDetailRes>> getRestaurantAndMenuDetail(@PathVariable("id") UUID id) {
        var result = getRestaurantAndMenuUsecase.getRestaurantAndMenuDetail(id);
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                result
        );
    }
}
