package com.candyshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.candyshop.entity.Cart;
import com.candyshop.entity.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
