package com.example.learner.domain.image.dto.request;

public record ImageInsertReq(
        Long id,
        String name,
        String imageUrl
) {
}
