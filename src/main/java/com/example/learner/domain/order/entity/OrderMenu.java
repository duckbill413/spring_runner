package com.example.learner.domain.order.entity;

import com.example.learner.domain.BaseEntity;
import com.example.learner.domain.menu.entity.Menu;
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
public class OrderMenu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_menu_id")
    private Long id;
    private Long count;
    private Long price;
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    @ManyToOne
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;

        if (!orderDetail.getOrderMenus().contains(this)) {
            orderDetail.getOrderMenus().add(this);
        }
    }
}
