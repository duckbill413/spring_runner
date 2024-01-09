package com.example.learner.domain.order.dao;

import com.example.learner.domain.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
}
