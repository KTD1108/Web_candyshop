package com.candyshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.candyshop.entity.Order;
import com.candyshop.entity.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtAsc(User user);
    List<Order> findByUser(User user);
}
