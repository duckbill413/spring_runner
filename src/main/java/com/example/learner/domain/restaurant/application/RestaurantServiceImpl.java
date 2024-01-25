package com.example.learner.domain.restaurant.application;

import com.example.learner.domain.image.dto.response.ImageDetailRes;
import com.example.learner.domain.image.entity.Image;
import com.example.learner.domain.restaurant.dao.RestaurantRepository;
import com.example.learner.domain.restaurant.dto.request.RestaurantInsertReq;
import com.example.learner.domain.restaurant.dto.resopnse.RestaurantDetailRes;
import com.example.learner.domain.restaurant.entity.Restaurant;
import com.example.learner.global.common.code.ErrorCode;
import com.example.learner.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;

    @Transactional
    @Override
    public void insertRestaurant(RestaurantInsertReq restaurantInsertReq) {
        Image image = Image.builder()
                .name(restaurantInsertReq.image().name())
                .imageUrl(restaurantInsertReq.image().imageUrl())
                .build();
        Restaurant restaurant = Restaurant.builder()
                .name(restaurantInsertReq.name())
                .phone(restaurantInsertReq.phone())
                .category(restaurantInsertReq.category())
                .image(image)
                .build();

        restaurantRepository.save(restaurant);
    }

    @Transactional
    @Override
    public void updateRestaurant(UUID id, RestaurantInsertReq restaurantInsertReq) {
        Image image = Image.builder()
                .id(restaurantInsertReq.image().id())
                .name(restaurantInsertReq.image().name())
                .imageUrl(restaurantInsertReq.image().imageUrl())
                .build();
        Restaurant restaurant = Restaurant.builder()
                .id(id)
                .name(restaurantInsertReq.name())
                .phone(restaurantInsertReq.phone())
                .category(restaurantInsertReq.category())
                .image(image)
                .build();
        restaurantRepository.save(restaurant);
    }

    @Transactional(readOnly = true)
    @Override
    public RestaurantDetailRes findRestaurantDetail(UUID id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() ->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_ERROR));
        return RestaurantDetailRes.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .phone(restaurant.getPhone())
                .category(restaurant.getCategory())
                .image(new ImageDetailRes(restaurant.getImage().getName(), restaurant.getImage().getImageUrl()))
                .build();
    }
}
