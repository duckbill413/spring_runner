package com.example.learner.domain.restaurant.dao;

import com.example.learner.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
    @Query(value = "SELECT r FROM Restaurant r JOIN FETCH r.menus")
    List<Restaurant> findAllWithMenu();
}
