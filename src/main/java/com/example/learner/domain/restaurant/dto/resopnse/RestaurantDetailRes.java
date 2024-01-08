package com.example.learner.domain.restaurant.dto.resopnse;

import com.example.learner.domain.image.dto.response.ImageDetailRes;
import com.example.learner.domain.restaurant.entity.Category;

import java.util.UUID;

public record RestaurantDetailRes(
        UUID id,
        String name,
        String phone,
        Category category,
        ImageDetailRes image
) {
}
