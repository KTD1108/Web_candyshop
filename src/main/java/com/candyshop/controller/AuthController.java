package com.candyshop.controller;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.candyshop.dto.LoginRequest;
import com.candyshop.dto.RegisterRequest;
import com.candyshop.security.CustomUserDetails;
import com.candyshop.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

	private final AuthService authService;

	/** Đăng ký tài khoản mới (ROLE_USER) */
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest dto) {
		authService.register(dto);
		return ResponseEntity.ok(Map.of("message", "registered"));
	}

	/** Đăng nhập -> trả về JWT accessToken */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest dto) {
		return ResponseEntity.ok(authService.login(dto));
	}

	/** Lấy thông tin người dùng hiện tại (từ JWT) */
	@GetMapping("/me")
	public ResponseEntity<?> me(@AuthenticationPrincipal CustomUserDetails cud) {
		if (cud == null) {
			return ResponseEntity.ok(Map.of("authenticated", false));
		}
		return ResponseEntity.ok(Map.of("authenticated", true, "email", cud.getUsername(), "fullName",
				cud.getUser().getFullName(), "roles", cud.getAuthorities()));
	}

	/** Validate token and check admin status */
	@GetMapping("/validate")
	public ResponseEntity<?> validate() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"));
		boolean isAdmin = false;

		if (isAuthenticated) {
			isAdmin = authentication.getAuthorities().stream()
					.map(GrantedAuthority::getAuthority)
					.anyMatch(role -> role.equals("ROLE_ADMIN"));
		}
		return ResponseEntity.ok(Map.of("isAuthenticated", isAuthenticated, "isAdmin", isAdmin));
	}
}
