package com.candyshop.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.candyshop.dto.UserUpdateRequest;
import com.candyshop.entity.User;
import com.candyshop.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin
public class AdminUserController {

	private final UserService userService;

	// Xử lý yêu cầu GET để lấy tất cả người dùng.
	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	// Xử lý yêu cầu GET để lấy thông tin một người dùng cụ thể dựa trên ID.
	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

	// Xử lý yêu cầu POST để tạo người dùng mới.
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody UserUpdateRequest request) {
		User createdUser = userService.createUser(request);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}

	// Xử lý yêu cầu PUT để cập nhật thông tin người dùng hiện có dựa trên ID.
	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
		User updatedUser = userService.updateUser(id, request);
		return ResponseEntity.ok(updatedUser);
	}

	// Xử lý yêu cầu DELETE để xóa một người dùng dựa trên ID.
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
}
