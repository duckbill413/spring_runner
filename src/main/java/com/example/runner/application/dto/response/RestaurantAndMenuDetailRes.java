package com.example.runner.application.dto.response;

import com.example.runner.domain.menu.dto.response.MenuDetailRes;
import com.example.runner.domain.restaurant.dto.resopnse.RestaurantDetailRes;

import java.util.List;

public record RestaurantAndMenuDetailRes(
        RestaurantDetailRes restaurant,
        List<MenuDetailRes> menus
) {
}
