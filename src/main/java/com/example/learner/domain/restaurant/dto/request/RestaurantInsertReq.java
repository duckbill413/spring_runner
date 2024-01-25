package com.example.learner.domain.restaurant.dto.request;

import com.example.learner.domain.image.dto.request.ImageInsertReq;
import com.example.learner.domain.restaurant.entity.Category;

public record RestaurantInsertReq(
        String name,
        String phone,
        Category category,
        ImageInsertReq image

) {
}
