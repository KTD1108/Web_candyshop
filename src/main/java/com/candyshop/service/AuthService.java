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
public class AuthService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public void register(RegisterRequest dto) {
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        User u = new User();
        u.setEmail(dto.getEmail());
        u.setPasswordHash(encoder.encode(dto.getPassword()));
        u.setFullName(dto.getFullName());
        u.setEnabled(true);

        Role r = roleRepo.findByName("ROLE_USER").orElseThrow();
        u.setRoles(new HashSet<>());
        u.getRoles().add(r);
        userRepo.save(u);
    }

    public Map<String,Object> login(LoginRequest dto) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        CustomUserDetails cud = (CustomUserDetails) auth.getPrincipal();
        String token = jwtService.generateToken(cud);
        return Map.of(
                "accessToken", token,
                "email", cud.getUsername(),
                "fullName", cud.getUser().getFullName(),
                "roles", cud.getAuthorities()
        );
    }
}
