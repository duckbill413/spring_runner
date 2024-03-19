package com.example.runner.domain.restaurant.api;

import com.example.runner.domain.restaurant.application.RestaurantService;
import com.example.runner.domain.restaurant.dto.request.RestaurantInsertReq;
import com.example.runner.domain.restaurant.dto.resopnse.RestaurantDetailRes;
import com.example.runner.global.common.BaseResponse;
import com.example.runner.global.common.code.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping("")
    public ResponseEntity<BaseResponse<String>> insertRestaurant(@RequestBody RestaurantInsertReq restaurantInsertReq) {
        restaurantService.insertRestaurant(restaurantInsertReq);
        return BaseResponse.success(
                SuccessCode.INSERT_SUCCESS,
                "레스토랑 등록 성공!"
        );
    }

    @PutMapping("/{restaurant-id}")
    public ResponseEntity<BaseResponse<String>> updateRestaurant(
            @PathVariable(value = "restaurant-id") UUID id,
            @RequestBody RestaurantInsertReq restaurantInsertReq
    ) {
        restaurantService.updateRestaurant(id, restaurantInsertReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                "레스토랑 정보 업데이트 성공!"
        );
    }
    @GetMapping("/{restaurant-id}")
    public ResponseEntity<BaseResponse<RestaurantDetailRes>> findRestaurantDetail(
            @PathVariable(value = "restaurant-id") UUID id
    ) {
        RestaurantDetailRes result = restaurantService.findRestaurantDetail(id);
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                result
        );
    }
}
