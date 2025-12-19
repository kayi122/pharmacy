package com.example.pharmacymanagementsystem.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pharmacymanagementsystem.dto.UserDTO;
import com.example.pharmacymanagementsystem.exception.DuplicateResourceException;
import com.example.pharmacymanagementsystem.exception.ResourceNotFoundException;
import com.example.pharmacymanagementsystem.model.Location;
import com.example.pharmacymanagementsystem.model.User;
import com.example.pharmacymanagementsystem.repository.LocationRepository;
import com.example.pharmacymanagementsystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getAllUsersWithRelations() {
        log.info("Fetching all users with relations");
        return userRepository.findAllWithRelations().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        log.info("Fetching user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToDTO(user);
    }

    public UserDTO getUserByIdWithRelations(Long id) {
        log.info("Fetching user with ID and relationships: {}", id);
        User user = userRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToDTO(user);
    }

    public UserDTO getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return convertToDTO(user);
    }

    public UserDTO getUserByEmailIgnoreCase(String email) {
        log.info("Fetching user with email (case insensitive): {}", email);
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return convertToDTO(user);
    }

    public UserDTO getUserByPhone(String phone) {
        log.info("Fetching user with phone: {}", phone);
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with phone: " + phone));
        return convertToDTO(user);
    }

    public List<UserDTO> searchByFirstName(String firstName) {
        log.info("Searching users with first name: {}", firstName);
        return userRepository.findByFirstNameContainingIgnoreCase(firstName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> searchByLastName(String lastName) {
        log.info("Searching users with last name: {}", lastName);
        return userRepository.findByLastNameContainingIgnoreCase(lastName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> searchByName(String name) {
        log.info("Searching users by name: {}", name);
        return userRepository.searchByName(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getUsersByRole(String role) {
        log.info("Fetching users with role: {}", role);
        return userRepository.findByRole(role).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getUsersByRoleOrderedByName(String role) {
        log.info("Fetching users with role {} ordered by name", role);
        return userRepository.findByRoleOrderByLastNameAscFirstNameAsc(role).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getUsersByLocation(Long locationId) {
        log.info("Fetching users in location: {}", locationId);
        return userRepository.findByLocationId(locationId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getUsersByLocationWithDetails(Long locationId) {
        log.info("Fetching users in location with details: {}", locationId);
        return userRepository.findByLocationIdWithDetails(locationId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getAllUsersOrderedByName() {
        log.info("Fetching all users ordered by name");
        return userRepository.findAllByOrderByLastNameAscFirstNameAsc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getUsersWithoutProfile() {
        log.info("Fetching users without profile");
        return userRepository.findUsersWithoutProfile().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getUsersWithSales() {
        log.info("Fetching users with sales");
        return userRepository.findUsersWithSales().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO createUser(User user) {
        log.info("Creating new user with email: {}", user.getEmail());

        // Check for duplicate email
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("User with email " + user.getEmail() + " already exists");
        }

        // Check for duplicate phone
        if (userRepository.existsByPhone(user.getPhone())) {
            throw new DuplicateResourceException("User with phone " + user.getPhone() + " already exists");
        }

        if (user.getLocation() != null && user.getLocation().getId() != null) {
            Location location = locationRepository.findById(user.getLocation().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Location not found"));
            user.setLocation(location);
        }

        // Hash password before saving
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());
        return convertToDTO(savedUser);
    }

    @Transactional
    public UserDTO updateUser(Long id, User updatedUser) {
        log.info("Updating user with ID: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Check for duplicate email (excluding current user)
        if (!existingUser.getEmail().equals(updatedUser.getEmail())
                && userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new DuplicateResourceException("User with email " + updatedUser.getEmail() + " already exists");
        }

        // Check for duplicate phone (excluding current user)
        if (!existingUser.getPhone().equals(updatedUser.getPhone())
                && userRepository.existsByPhone(updatedUser.getPhone())) {
            throw new DuplicateResourceException("User with phone " + updatedUser.getPhone() + " already exists");
        }

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setRole(updatedUser.getRole());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        if (updatedUser.getLocation() != null && updatedUser.getLocation().getId() != null) {
            Location location = locationRepository.findById(updatedUser.getLocation().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Location not found"));
            existingUser.setLocation(location);
        }

        User savedUser = userRepository.save(existingUser);
        log.info("User updated successfully with ID: {}", savedUser.getId());
        return convertToDTO(savedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
        log.info("User deleted successfully with ID: {}", id);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    public boolean hasProfile(Long userId) {
        return userRepository.hasProfile(userId);
    }

    public long countAllUsers() {
        return userRepository.count();
    }

    public long countUsersByRole(String role) {
        return userRepository.countByRole(role);
    }

    public long countUsersByLocation(Long locationId) {
        return userRepository.countByLocationId(locationId);
    }

    @Transactional
    public void hashPlainTextPasswords() {
        log.info("Starting to hash plain text passwords");
        List<User> allUsers = userRepository.findAll();
        
        for (User user : allUsers) {
            String password = user.getPassword();
            // Check if password is already hashed (BCrypt hashes start with $2a$, $2b$, or $2y$)
            if (password != null && !password.startsWith("$2")) {
                log.info("Hashing password for user: {}", user.getEmail());
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
            }
        }
        log.info("Completed hashing plain text passwords");
    }

    public long countSalesByUser(Long userId) {
        return userRepository.countSalesByUser(userId);
    }

    public Double getTotalRevenueByUser(Long userId) {
        return userRepository.getTotalRevenueByUser(userId);
    }

    public List<Object[]> getTopPerformingUsersBySalesCount() {
        return userRepository.findTopPerformingUsersBySalesCount();
    }

    public List<Object[]> getTopPerformingUsersByRevenue() {
        return userRepository.findTopPerformingUsersByRevenue();
    }

    public List<UserDTO> searchUsers(String query) {
        log.info("Searching users with query: {}", query);
        return userRepository.searchByName(query).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user);
    }
}
