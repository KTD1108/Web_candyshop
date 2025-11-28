package com.candyshop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.candyshop.dto.ChangePasswordRequest;
import com.candyshop.dto.ProfileUpdateRequest;
import com.candyshop.entity.User;
import com.candyshop.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin
// Lớp Controller xử lý các yêu cầu liên quan đến quản lý hồ sơ và cài đặt của người dùng.
public class UserController {

	private final UserService userService;

	// Xử lý yêu cầu GET để lấy thông tin hồ sơ của người dùng hiện tại.
	@GetMapping("/profile")
	public ResponseEntity<User> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
		User user = userService.getUserByEmail(userDetails.getUsername());
		user.setPasswordHash(null); // Không trả về hash mật khẩu để đảm bảo bảo mật
		return ResponseEntity.ok(user);
	}

	// Xử lý yêu cầu PUT để cập nhật thông tin hồ sơ của người dùng hiện tại.
	@PutMapping("/profile")
	public ResponseEntity<Void> updateProfile(@AuthenticationPrincipal UserDetails userDetails,
			@RequestBody ProfileUpdateRequest request) {
		// Implementation needed in UserService
		userService.updateProfile(userDetails.getUsername(), request);
		return ResponseEntity.ok().build();
	}

	// Xử lý yêu cầu PUT để thay đổi mật khẩu của người dùng hiện tại.
	@PutMapping("/password")
	public ResponseEntity<Void> changePassword(@AuthenticationPrincipal UserDetails userDetails,
			@RequestBody ChangePasswordRequest request) {
		// Implementation needed in UserService
		userService.changePassword(userDetails.getUsername(), request);
		return ResponseEntity.ok().build();
	}
}
