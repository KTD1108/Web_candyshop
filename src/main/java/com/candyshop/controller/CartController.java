package com.candyshop.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.candyshop.dto.AddCartItemRequest;
import com.candyshop.dto.UpdateCartItemRequest;
import com.candyshop.entity.Cart;
import com.candyshop.entity.CartItem;
import com.candyshop.entity.User;
import com.candyshop.security.CustomUserDetails;
import com.candyshop.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin
// Lớp Controller xử lý các yêu cầu liên quan đến quản lý giỏ hàng của người dùng.
public class CartController {

	private final CartService cartService;

	// Xử lý yêu cầu GET để lấy giỏ hàng của người dùng hiện tại.
	@GetMapping
	public ResponseEntity<Cart> getCart(@AuthenticationPrincipal CustomUserDetails cud) {
		User u = cud.getUser();
		Cart c = cartService.ensureCart(u);
		return ResponseEntity.ok(c);
	}

	// Xử lý yêu cầu POST để thêm sản phẩm vào giỏ hàng.
	@PostMapping("/items")
	public ResponseEntity<?> addItem(@AuthenticationPrincipal CustomUserDetails cud,
			@RequestBody AddCartItemRequest req) {
		cartService.addItem(cud.getUser(), req);
		return ResponseEntity.ok(Map.of("message", "added"));
	}

	// Xử lý yêu cầu PUT để cập nhật số lượng của một mục trong giỏ hàng.
	@PutMapping("/items/{cartItemId}")
	public ResponseEntity<?> updateItem(@PathVariable Long cartItemId, @RequestBody @Valid UpdateCartItemRequest req,
			@AuthenticationPrincipal CustomUserDetails cud) {
		CartItem updatedItem = cartService.updateCartItemQuantity(cartItemId, req.getQuantity(), cud.getUser());
		if (updatedItem == null) {
			return ResponseEntity.ok(Map.of("message", "deleted"));
		}
		return ResponseEntity.ok(updatedItem);
	}

	// Xử lý yêu cầu DELETE để xóa một mục khỏi giỏ hàng.
	@DeleteMapping("/items/{itemId}")
	public ResponseEntity<?> deleteItem(@PathVariable Long itemId, @AuthenticationPrincipal CustomUserDetails cud) {
		cartService.deleteItem(itemId, cud.getUser());
		return ResponseEntity.ok(Map.of("message", "deleted"));
	}
}
