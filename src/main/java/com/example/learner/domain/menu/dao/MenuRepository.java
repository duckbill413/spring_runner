package com.example.learner.domain.menu.dao;

import com.example.learner.domain.menu.dto.response.MenuDetailRes;
import com.example.learner.domain.menu.entity.Menu;
import com.example.learner.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByRestaurant(Restaurant restaurant);

    List<Menu> findByRestaurant_Name(String name);

    @Query(value = """
            SELECT m
            FROM Menu m
            WHERE m.restaurant.id = :id
            """,
            countQuery = """
                    SELECT count(m)
                    FROM Menu m
                    WHERE m.restaurant.id = :id
                    """)
    List<Menu> findByRestaurantId(@Param("id") UUID id);

    @Query(value = """
            SELECT new com.example.learner.domain.menu.dto.response.MenuDetailRes(
            m.id, m.name, m.price, m.stock)
            FROM Menu m
            WHERE m.id = :id
            """)
    MenuDetailRes findByIdToMenuDetailRes(@Param("id") UUID id);
}
