package com.example.learner.domain.restaurant.dao;

import com.example.learner.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
}
