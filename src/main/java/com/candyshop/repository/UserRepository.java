package com.candyshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.candyshop.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
