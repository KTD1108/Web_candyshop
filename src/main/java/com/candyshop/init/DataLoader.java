package com.candyshop.init;

import java.util.HashSet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional; // <== thêm

import com.candyshop.entity.Role;
import com.candyshop.entity.User;
import com.candyshop.repository.CartItemRepository;
import com.candyshop.repository.CartRepository;
import com.candyshop.repository.CategoryRepository;
import com.candyshop.repository.OrderItemRepository;
import com.candyshop.repository.OrderRepository;
import com.candyshop.repository.ProductRepository;
import com.candyshop.repository.RoleRepository;
import com.candyshop.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
// Lớp này chịu trách nhiệm tải dữ liệu khởi tạo vào cơ sở dữ liệu khi ứng dụng bắt đầu chạy.
public class DataLoader implements CommandLineRunner {

	private final RoleRepository roleRepo;
	private final UserRepository userRepo;
	private final PasswordEncoder encoder;
	private final CategoryRepository catRepo;
	private final ProductRepository prodRepo;
	private final CartItemRepository cartItemRepo;
	private final CartRepository cartRepo;
	private final OrderItemRepository orderItemRepo;
	private final OrderRepository orderRepo;

	// Phương thức này được thực thi tự động khi ứng dụng Spring Boot khởi động.
	@Override
	@Transactional
	public void run(String... args) throws Exception {

		// Đảm bảo các vai trò (ROLE_USER, ROLE_ADMIN) tồn tại trong cơ sở dữ liệu.
		roleRepo.findByName("ROLE_USER").orElseGet(() -> {
			Role r = new Role();
			r.setName("ROLE_USER");
			return roleRepo.save(r);
		});
		roleRepo.findByName("ROLE_ADMIN").orElseGet(() -> {
			Role r = new Role();
			r.setName("ROLE_ADMIN");
			return roleRepo.save(r);
		});

		// Tạo người dùng admin mặc định nếu chưa tồn tại.
		userRepo.findByEmail("admin@gmail.com").orElseGet(() -> {
			User admin = new User();
			admin.setEmail("admin@gmail.com");
			admin.setPasswordHash(encoder.encode("admin123"));
			admin.setFullName("Administrator");
			admin.setEnabled(true);
			admin.setRoles(new HashSet<>());
			admin.getRoles().add(roleRepo.findByName("ROLE_ADMIN").orElseThrow());
			admin.getRoles().add(roleRepo.findByName("ROLE_USER").orElseThrow());
			return userRepo.save(admin);
		});

	}

}
