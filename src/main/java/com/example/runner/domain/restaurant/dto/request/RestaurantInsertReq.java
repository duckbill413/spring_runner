package com.example.runner.domain.restaurant.dto.request;

import com.example.runner.domain.image.dto.request.ImageInsertReq;
import com.example.runner.domain.restaurant.entity.Category;

public record RestaurantInsertReq(
        String name,
        String phone,
        Category category,
        ImageInsertReq image

) {
}
