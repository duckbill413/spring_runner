package com.example.learner.domain.menu.entity;

import com.example.learner.domain.BaseEntity;
import com.example.learner.domain.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Menu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;
    private String name;
    private Long price;
    private Long stock;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;

        if (!restaurant.getMenus().contains(this)) {
            restaurant.getMenus().add(this);
        }
    }
}
