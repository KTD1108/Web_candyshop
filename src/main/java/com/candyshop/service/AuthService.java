package com.candyshop.service;

import java.util.HashSet;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.candyshop.dto.LoginRequest;
import com.candyshop.dto.RegisterRequest;
import com.candyshop.entity.Role;
import com.candyshop.entity.User;
import com.candyshop.repository.RoleRepository;
import com.candyshop.repository.UserRepository;
import com.candyshop.security.CustomUserDetails;
import com.candyshop.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
//  xử lý logic nghiệp vụ liên quan đến xác thực người dùng.
public class AuthService {
	private final UserRepository userRepo;
	private final RoleRepository roleRepo;
	private final PasswordEncoder encoder;
	private final JwtService jwtService;
	private final AuthenticationManager authManager;

	// Xử lý logic đăng ký người dùng mới.
	public void register(RegisterRequest dto) {
		// Kiểm tra xem email đã được đăng ký chưa.
		if (userRepo.existsByEmail(dto.getEmail())) {
			throw new RuntimeException("Email already registered");
		}
		User u = new User();
		u.setEmail(dto.getEmail());
		u.setPasswordHash(encoder.encode(dto.getPassword()));
		u.setFullName(dto.getFullName());
		u.setEnabled(true);

		// Gán vai trò mặc định là ROLE_USER cho người dùng mới.
		Role r = roleRepo.findByName("ROLE_USER").orElseThrow();
		u.setRoles(new HashSet<>());
		u.getRoles().add(r);
		userRepo.save(u);
	}

	// Xử lý logic đăng nhập và trả về thông tin người dùng cùng với token JWT.
	public Map<String, Object> login(LoginRequest dto) {
		// Xác thực người dùng bằng email và mật khẩu.
		Authentication auth = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
		CustomUserDetails cud = (CustomUserDetails) auth.getPrincipal();
		// Tạo token JWT.
		String token = jwtService.generateToken(cud);
		// Trả về map chứa token và thông tin người dùng.
		return Map.of("accessToken", token, "email", cud.getUsername(), "fullName", cud.getUser().getFullName(),
				"roles", cud.getAuthorities());
	}
}
