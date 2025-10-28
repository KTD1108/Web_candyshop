package com.candyshop.repository;

import com.candyshop.entity.Cart;
import com.candyshop.entity.CartItem;
import com.candyshop.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
