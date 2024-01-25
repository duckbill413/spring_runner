package com.example.learner.domain.restaurant.application;

import com.example.learner.domain.restaurant.dto.request.RestaurantInsertReq;
import com.example.learner.domain.restaurant.dto.resopnse.RestaurantDetailRes;

import java.util.UUID;

public interface RestaurantService {
    void insertRestaurant(RestaurantInsertReq restaurantInsertReq);
    void updateRestaurant(UUID id, RestaurantInsertReq restaurantInsertReq);
    RestaurantDetailRes findRestaurantDetail(UUID id);
}
