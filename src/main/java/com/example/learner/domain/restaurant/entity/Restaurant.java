package com.example.learner.domain.restaurant.entity;

import com.example.learner.domain.BaseEntity;
import com.example.learner.domain.image.entity.Image;
import com.example.learner.domain.menu.entity.Menu;
import com.example.learner.domain.order.entity.OrderDetail;
import com.example.learner.domain.restaurant.dto.resopnse.RestaurantDetailRes;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Restaurant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // (springboot 3.0 & hibernate6 에서만 지원)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "restaurant_id")
    private UUID id;
    private String name;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Category category;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private Image image;
    @OneToMany(mappedBy = "restaurant", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @Builder.Default
    private List<Menu> menus = new ArrayList<>();
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public static RestaurantDetailRes getDetailDto(Restaurant restaurant) {
        return new RestaurantDetailRes(restaurant.getId(),
                restaurant.getName(),
                restaurant.getPhone(),
                restaurant.getCategory(),
                Image.getDto(restaurant.getImage())
        );
    }
}
