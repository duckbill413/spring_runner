package com.example.learner.application.dto.response;

import com.example.learner.domain.menu.dto.response.MenuDetailRes;
import com.example.learner.domain.restaurant.dto.resopnse.RestaurantDetailRes;

import java.util.List;

public record RestaurantAndMenuDetailRes(
        RestaurantDetailRes restaurant,
        List<MenuDetailRes> menus
) {
}
