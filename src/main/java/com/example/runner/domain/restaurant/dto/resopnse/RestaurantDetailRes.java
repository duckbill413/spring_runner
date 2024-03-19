package com.example.runner.domain.restaurant.dto.resopnse;

import com.example.runner.domain.image.dto.response.ImageDetailRes;
import com.example.runner.domain.restaurant.entity.Category;
import lombok.Builder;

import java.util.UUID;

@Builder
public record RestaurantDetailRes(
        UUID id,
        String name,
        String phone,
        Category category,
        ImageDetailRes image
) {
}
