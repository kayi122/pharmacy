package com.example.pharmacymanagementsystem.controller;

import com.example.pharmacymanagementsystem.model.User;
import com.example.pharmacymanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/hash-passwords")
    public String hashExistingPasswords() {
        userRepository.findAll().forEach(user -> {
            String currentPassword = user.getPassword();
            if (!currentPassword.startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(currentPassword));
                userRepository.save(user);
            }
        });
        return "All passwords have been hashed with BCrypt";
    }
}