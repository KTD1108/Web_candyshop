package com.candyshop.controller;

import com.candyshop.entity.Role;
import com.candyshop.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@CrossOrigin
// Lớp Controller xử lý các yêu cầu liên quan đến quản lý và truy xuất vai trò người dùng.
public class RoleController {

    private final RoleRepository roleRepository;

    // Xử lý yêu cầu GET để lấy tất cả các vai trò người dùng.
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleRepository.findAll());
    }
}
