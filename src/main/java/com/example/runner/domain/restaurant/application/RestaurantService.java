package com.example.runner.domain.restaurant.application;

import com.example.runner.domain.restaurant.dto.request.RestaurantInsertReq;
import com.example.runner.domain.restaurant.dto.resopnse.RestaurantDetailRes;

import java.util.UUID;

public interface RestaurantService {
    void insertRestaurant(RestaurantInsertReq restaurantInsertReq);
    void updateRestaurant(UUID id, RestaurantInsertReq restaurantInsertReq);
    RestaurantDetailRes findRestaurantDetail(UUID id);
}
