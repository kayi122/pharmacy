package com.example.pharmacymanagementsystem.controller;

import com.example.pharmacymanagementsystem.model.User;
import com.example.pharmacymanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DebugController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/admin-status")
    public Map<String, Object> checkAdminStatus() {
        Map<String, Object> response = new HashMap<>();
        
        Optional<User> adminUser = userRepository.findByEmail("admin@pharmacy.com");
        
        if (adminUser.isPresent()) {
            User user = adminUser.get();
            response.put("adminExists", true);
            response.put("email", user.getEmail());
            response.put("firstName", user.getFirstName());
            response.put("role", user.getRole());
            response.put("active", user.getActive());
            response.put("emailVerified", user.getEmailVerified());
            response.put("twoFactorEnabled", user.getTwoFactorEnabled());
            
            // Test password
            boolean passwordMatches = passwordEncoder.matches("password123", user.getPassword());
            response.put("passwordMatches", passwordMatches);
            response.put("passwordHash", user.getPassword().substring(0, 20) + "...");
        } else {
            response.put("adminExists", false);
        }
        
        return response;
    }

    @PostMapping("/create-admin")
    public Map<String, Object> createAdmin() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check if admin already exists
            if (userRepository.existsByEmail("admin@pharmacy.com")) {
                response.put("success", false);
                response.put("message", "Admin user already exists");
                return response;
            }

            User admin = User.builder()
                    .firstName("Admin")
                    .lastName("User")
                    .email("admin@pharmacy.com")
                    .phone("0781234567")
                    .password(passwordEncoder.encode("password123"))
                    .role("ADMIN")
                    .location(null)
                    .active(true)
                    .emailVerified(true)
                    .twoFactorEnabled(false)
                    .build();

            userRepository.save(admin);
            
            response.put("success", true);
            response.put("message", "Admin user created successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating admin: " + e.getMessage());
        }
        
        return response;
    }
}