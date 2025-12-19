package com.example.pharmacymanagementsystem.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.pharmacymanagementsystem.model.Location;
import com.example.pharmacymanagementsystem.model.User;
import com.example.pharmacymanagementsystem.repository.LocationRepository;
import com.example.pharmacymanagementsystem.repository.UserRepository;

@Component
@Order(2)
public class AdminUserInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        try {
            if (!userRepository.existsByEmail("admin@pharmacy.com")) {
                Location village = locationRepository.findByType(Location.LocationType.VILLAGE)
                        .stream().findFirst().orElse(null);

                User admin = User.builder()
                        .firstName("Admin")
                        .lastName("User")
                        .email("admin@pharmacy.com")
                        .phone("0781234567")
                        .password(passwordEncoder.encode("password123"))
                        .role("ADMIN")
                        .location(village)
                        .active(true)
                        .emailVerified(true)
                        .twoFactorEnabled(false)
                        .build();

                userRepository.save(admin);
                System.out.println("✓ Admin user created successfully!");
                System.out.println("  Email: admin@pharmacy.com");
                System.out.println("  Password: password123");
            }
        } catch (Exception e) {
            System.err.println("Error creating admin user: " + e.getMessage());
        }
    }
}
