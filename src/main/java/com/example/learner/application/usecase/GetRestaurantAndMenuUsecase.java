package com.example.learner.application.usecase;

import com.example.learner.application.dto.response.RestaurantAndMenuDetailRes;
import com.example.learner.domain.menu.dao.MenuRepository;
import com.example.learner.domain.menu.entity.Menu;
import com.example.learner.domain.restaurant.dao.RestaurantRepository;
import com.example.learner.domain.restaurant.entity.Restaurant;
import com.example.learner.global.common.code.ErrorCode;
import com.example.learner.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class GetRestaurantAndMenuUsecase {
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    public RestaurantAndMenuDetailRes getRestaurantAndMenuDetail(UUID restaurantId) {
        var restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() ->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_RESTAURANT));

        var menus = menuRepository.findByRestaurant(restaurant);

        return new RestaurantAndMenuDetailRes(
                Restaurant.getDetailDto(restaurant),
                menus.stream().map(Menu::getDetailRes).toList()
        );
    }
}
