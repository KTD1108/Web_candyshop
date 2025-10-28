package com.candyshop.service;

import com.candyshop.dto.UserUpdateRequest;
import com.candyshop.entity.Cart;
import com.candyshop.entity.Order;
import com.candyshop.entity.Role;
import com.candyshop.entity.User;
import com.candyshop.repository.CartRepository;
import com.candyshop.repository.OrderRepository;
import com.candyshop.repository.RoleRepository;
import com.candyshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User createUser(UserUpdateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        // For simplicity, a default password or a generated one could be set here.
        // In a real application, this would involve a more secure password management.
        user.setPasswordHash(passwordEncoder.encode("defaultPassword")); // Set a default password
        user.setFullName(request.getFullName());
        user.setEnabled(request.getEnabled());

        Set<Role> roles = request.getRoleIds().stream()
                .map(roleId -> roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found")))
                .collect(Collectors.toSet());
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, UserUpdateRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setFullName(request.getFullName());
        existingUser.setEnabled(request.getEnabled());

        Set<Role> roles = request.getRoleIds().stream()
                .map(roleId -> roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found")))
                .collect(Collectors.toSet());
        existingUser.setRoles(roles);

        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Disassociate orders from the user (set user_id to null)
        List<Order> userOrders = orderRepository.findByUser(user);
        for (Order order : userOrders) {
            order.setUser(null);
            orderRepository.save(order);
        }

        // Delete user's cart if exists
        cartRepository.findByUser(user).ifPresent(cartRepository::delete);

        userRepository.delete(user);
    }
}
