package com.example.pharmacymanagementsystem.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pharmacymanagementsystem.dto.UserDTO;
import com.example.pharmacymanagementsystem.model.User;
import com.example.pharmacymanagementsystem.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("GET request to fetch all users");
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/with-relations")
    public ResponseEntity<List<UserDTO>> getAllUsersWithRelations() {
        log.info("GET request to fetch all users with relations");
        List<UserDTO> users = userService.getAllUsersWithRelations();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        log.info("GET request to fetch user with ID: {}", id);
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/with-relations")
    public ResponseEntity<UserDTO> getUserByIdWithRelations(@PathVariable Long id) {
        log.info("GET request to fetch user with ID and relationships: {}", id);
        UserDTO user = userService.getUserByIdWithRelations(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        log.info("GET request to fetch user with email: {}", email);
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email-ignore-case")
    public ResponseEntity<UserDTO> getUserByEmailIgnoreCase(@RequestParam String email) {
        log.info("GET request to fetch user with email (case insensitive): {}", email);
        UserDTO user = userService.getUserByEmailIgnoreCase(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/phone")
    public ResponseEntity<UserDTO> getUserByPhone(@RequestParam String phone) {
        log.info("GET request to fetch user with phone: {}", phone);
        UserDTO user = userService.getUserByPhone(phone);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/search/firstname")
    public ResponseEntity<List<UserDTO>> searchByFirstName(@RequestParam String name) {
        log.info("GET request to search users by first name: {}", name);
        List<UserDTO> users = userService.searchByFirstName(name);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search/lastname")
    public ResponseEntity<List<UserDTO>> searchByLastName(@RequestParam String name) {
        log.info("GET request to search users by last name: {}", name);
        List<UserDTO> users = userService.searchByLastName(name);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchByName(@RequestParam String name) {
        log.info("GET request to search users by name: {}", name);
        List<UserDTO> users = userService.searchByName(name);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable String role) {
        log.info("GET request to fetch users with role: {}", role);
        List<UserDTO> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/role/{role}/ordered-by-name")
    public ResponseEntity<List<UserDTO>> getUsersByRoleOrderedByName(@PathVariable String role) {
        log.info("GET request to fetch users with role {} ordered by name", role);
        List<UserDTO> users = userService.getUsersByRoleOrderedByName(role);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<UserDTO>> getUsersByLocation(@PathVariable Long locationId) {
        log.info("GET request to fetch users in location: {}", locationId);
        List<UserDTO> users = userService.getUsersByLocation(locationId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/location/{locationId}/with-details")
    public ResponseEntity<List<UserDTO>> getUsersByLocationWithDetails(@PathVariable Long locationId) {
        log.info("GET request to fetch users in location with details: {}", locationId);
        List<UserDTO> users = userService.getUsersByLocationWithDetails(locationId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/ordered-by-name")
    public ResponseEntity<List<UserDTO>> getAllUsersOrderedByName() {
        log.info("GET request to fetch users ordered by name");
        List<UserDTO> users = userService.getAllUsersOrderedByName();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/without-profile")
    public ResponseEntity<List<UserDTO>> getUsersWithoutProfile() {
        log.info("GET request to fetch users without profile");
        List<UserDTO> users = userService.getUsersWithoutProfile();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/with-sales")
    public ResponseEntity<List<UserDTO>> getUsersWithSales() {
        log.info("GET request to fetch users with sales");
        List<UserDTO> users = userService.getUsersWithSales();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        log.info("POST request to create user with email: {}", user.getEmail());
        UserDTO createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User user) {
        log.info("PUT request to update user with ID: {}", id);
        UserDTO updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE request to delete user with ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        log.info("GET request to check if email exists: {}", email);
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/phone")
    public ResponseEntity<Boolean> checkPhoneExists(@RequestParam String phone) {
        log.info("GET request to check if phone exists: {}", phone);
        boolean exists = userService.existsByPhone(phone);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/{userId}/has-profile")
    public ResponseEntity<Boolean> hasProfile(@PathVariable Long userId) {
        log.info("GET request to check if user {} has profile", userId);
        boolean has = userService.hasProfile(userId);
        return ResponseEntity.ok(has);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countUsers() {
        log.info("GET request to count all users");
        long count = userService.countAllUsers();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/role/{role}")
    public ResponseEntity<Long> countUsersByRole(@PathVariable String role) {
        log.info("GET request to count users with role: {}", role);
        long count = userService.countUsersByRole(role);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/location/{locationId}")
    public ResponseEntity<Long> countUsersByLocation(@PathVariable Long locationId) {
        log.info("GET request to count users in location: {}", locationId);
        long count = userService.countUsersByLocation(locationId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{userId}/sales/count")
    public ResponseEntity<Long> countSalesByUser(@PathVariable Long userId) {
        log.info("GET request to count sales for user: {}", userId);
        long count = userService.countSalesByUser(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{userId}/sales/revenue")
    public ResponseEntity<Double> getTotalRevenueByUser(@PathVariable Long userId) {
        log.info("GET request to get total revenue for user: {}", userId);
        Double revenue = userService.getTotalRevenueByUser(userId);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/statistics/top-performers/sales-count")
    public ResponseEntity<List<Object[]>> getTopPerformingUsersBySalesCount() {
        log.info("GET request to get top performing users by sales count");
        List<Object[]> statistics = userService.getTopPerformingUsersBySalesCount();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/statistics/top-performers/revenue")
    public ResponseEntity<List<Object[]>> getTopPerformingUsersByRevenue() {
        log.info("GET request to get top performing users by revenue");
        List<Object[]> statistics = userService.getTopPerformingUsersByRevenue();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getUserStats() {
        log.info("GET request to get user statistics");
        long totalUsers = userService.countAllUsers();
        return ResponseEntity.ok(Map.of("totalUsers", totalUsers));
    }

    @GetMapping("/debug/{email}")
    public ResponseEntity<Object> debugUser(@PathVariable String email) {
        log.info("DEBUG request for user: {}", email);
        try {
            UserDTO user = userService.getUserByEmail(email);
            return ResponseEntity.ok(Map.of(
                "email", user.getEmail(),
                "active", user.getActive(),
                "emailVerified", user.getEmailVerified(),
                "passwordStartsWith", "[HIDDEN]"
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/migrate-passwords")
    public ResponseEntity<Object> migratePasswords() {
        log.info("POST request to migrate plain text passwords");
        userService.hashPlainTextPasswords();
        return ResponseEntity.ok(Map.of("message", "Password migration completed"));
    }
}
