package com.candyshop.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.candyshop.dto.UserUpdateRequest;
import com.candyshop.entity.Order;
import com.candyshop.entity.Role;
import com.candyshop.entity.User;
import com.candyshop.repository.CartRepository;
import com.candyshop.repository.OrderRepository;
import com.candyshop.repository.RoleRepository;
import com.candyshop.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
//  xử lý logic nghiệp vụ liên quan đến quản lý người dùng, bao gồm các hoạt động CRUD, cập nhật hồ sơ và thay đổi mật khẩu.
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final CartRepository cartRepository;
	private final OrderRepository orderRepository;

	// Lấy tất cả người dùng trong hệ thống.
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	// Lấy thông tin người dùng bằng ID.
	public User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
	}

	// Tạo một người dùng mới.
	@Transactional
	public User createUser(UserUpdateRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email already registered");
		}

		User user = new User();
		user.setEmail(request.getEmail());
		// Đặt mật khẩu mặc định và mã hóa.
		user.setPasswordHash(passwordEncoder.encode("defaultPassword"));
		user.setFullName(request.getFullName());
		user.setEnabled(request.getEnabled());

		// Gán các vai trò (roles) cho người dùng.
		Set<Role> roles = request.getRoleIds().stream().map(
				roleId -> roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found")))
				.collect(Collectors.toSet());
		user.setRoles(roles);

		return userRepository.save(user);
	}

	// Cập nhật thông tin của một người dùng hiện có.
	@Transactional
	public User updateUser(Long id, UserUpdateRequest request) {
		User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

		existingUser.setFullName(request.getFullName());
		existingUser.setEnabled(request.getEnabled());

		// Cập nhật các vai trò của người dùng.
		Set<Role> roles = request.getRoleIds().stream().map(
				roleId -> roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found")))
				.collect(Collectors.toSet());
		existingUser.setRoles(roles);

		return userRepository.save(existingUser);
	}

	// Xóa một người dùng khỏi hệ thống.
	@Transactional
	public void deleteUser(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

		// Hủy liên kết các đơn hàng của người dùng (đặt user_id thành null để không xóa
		// đơn hàng).
		List<Order> userOrders = orderRepository.findByUser(user);
		for (Order order : userOrders) {
			order.setUser(null);
			orderRepository.save(order);
		}

		// Xóa giỏ hàng của người dùng nếu tồn tại.
		cartRepository.findByUser(user).ifPresent(cartRepository::delete);

		userRepository.delete(user);
	}

	// Lấy thông tin người dùng bằng địa chỉ email.
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found for email: " + email));
	}

	// Cập nhật thông tin hồ sơ của người dùng hiện tại.
	@Transactional
	public void updateProfile(String email, com.candyshop.dto.ProfileUpdateRequest request) {
		User user = getUserByEmail(email);
		user.setFullName(request.getFullName());
		user.setAddress(request.getAddress());
		user.setPhone(request.getPhone());
		userRepository.save(user);
	}

	// Thay đổi mật khẩu của người dùng hiện tại.
	@Transactional
	public void changePassword(String email, com.candyshop.dto.ChangePasswordRequest request) {
		User user = getUserByEmail(email);
		// Kiểm tra mật khẩu cũ có đúng không.
		if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
			throw new RuntimeException("Invalid old password");
		}
		// Mã hóa và lưu mật khẩu mới.
		user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);
	}
}
