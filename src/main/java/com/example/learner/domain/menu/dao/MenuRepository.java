package com.example.learner.domain.menu.dao;

import com.example.learner.domain.menu.entity.Menu;
import com.example.learner.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByRestaurant(Restaurant restaurant);
}
