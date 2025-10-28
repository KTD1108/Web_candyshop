package com.candyshop.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.candyshop.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Page<Product> findAllByNameContaining(String kw, Pageable pageable);

	Page<Product> findAllByCategoryId(Long categoryId, Pageable pageable);

	Page<Product> findAllByNameContainingAndCategoryId(String kw, Long categoryId, Pageable pageable);

	Optional<Product> findBySlug(String slug);
}
