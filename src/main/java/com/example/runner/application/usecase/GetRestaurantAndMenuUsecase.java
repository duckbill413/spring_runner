package com.example.runner.application.usecase;

import com.example.runner.application.dto.response.RestaurantAndMenuDetailRes;
import com.example.runner.domain.menu.dao.MenuRepository;
import com.example.runner.domain.menu.entity.Menu;
import com.example.runner.domain.restaurant.dao.RestaurantRepository;
import com.example.runner.domain.restaurant.entity.Restaurant;
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
        var restaurant = restaurantRepository.findById(restaurantId).orElseThrow();

        var menus = menuRepository.findByRestaurant(restaurant);

        return new RestaurantAndMenuDetailRes(
                Restaurant.getDetailDto(restaurant),
                menus.stream().map(Menu::getDetailRes).toList()
        );
    }
}
