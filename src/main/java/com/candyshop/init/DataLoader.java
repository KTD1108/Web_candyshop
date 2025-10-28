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

	@Override
	@Transactional // <== thêm để đảm bảo thứ tự xóa/ghi trong 1 transaction
	public void run(String... args) throws Exception {
		// Ensure roles
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

		// Create default admin if not exists
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

		// --- DATA IS NOW PERSISTENT ---
		// The following code to reset and seed data has been removed as per user
		// request.
		// The application will now use the data that exists in the database.

		// orderItemRepo.deleteAllInBatch();
		// orderRepo.deleteAllInBatch();
		// cartItemRepo.deleteAllInBatch();
		// cartRepo.deleteAllInBatch();
		// prodRepo.deleteAllInBatch();
		// catRepo.deleteAllInBatch();

		// Seeding logic for categories and products has also been removed.
	}

	// The createProduct method is no longer needed.

}
