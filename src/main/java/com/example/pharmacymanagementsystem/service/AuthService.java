package com.example.pharmacymanagementsystem.service;

import com.example.pharmacymanagementsystem.dto.auth.*;
import com.example.pharmacymanagementsystem.model.Customer;
import com.example.pharmacymanagementsystem.model.Location;
import com.example.pharmacymanagementsystem.model.User;
import com.example.pharmacymanagementsystem.repository.CustomerRepository;
import com.example.pharmacymanagementsystem.repository.LocationRepository;
import com.example.pharmacymanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final LocationRepository locationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OTPService otpService;
    private final EmailService emailService;

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        // Validate email uniqueness
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone number already registered");
        }

        // Get location if provided
        Location location = null;
        if (request.getLocationId() != null) {
            location = locationRepository.findById(request.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found"));
        }

        // Normalize role to uppercase string
        String role = request.getRole().toUpperCase();

        // Create user
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role) // String, not enum
                .location(location)
                .active(true)
                .emailVerified(false)
                .twoFactorEnabled(true)
                .build();

        user = userRepository.save(user);
        log.info("User created: {}", user.getEmail());

        // Send OTP for email verification (handle failures gracefully)
        try {
            otpService.generateAndSendOTP(user.getEmail());
            log.info("OTP sent successfully to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send OTP, but user registration continues: {}", e.getMessage());
        }

        // Send welcome email (handle failures gracefully)
        try {
            emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName());
            log.info("Welcome email sent successfully to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send welcome email, but user registration continues: {}", e.getMessage());
        }

        return AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole()) // String
                .requiresOTP(true)
                .message("Registration successful! Please verify your email with the OTP sent.")
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        log.debug("Login attempt for user: {}", request.getEmail());
        log.debug("Stored password starts with: {}", user.getPassword().substring(0, Math.min(10, user.getPassword().length())));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Password mismatch for user: {}", request.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }

        if (!user.getActive()) {
            throw new RuntimeException("Account is deactivated");
        }

        // If 2FA is enabled, send OTP
        if (user.getTwoFactorEnabled()) {
            try {
                otpService.generateAndSendOTP(user.getEmail());
                return AuthResponse.builder()
                        .userId(user.getId())
                        .email(user.getEmail())
                        .requiresOTP(true)
                        .message("OTP sent to your email. Please verify to continue.")
                        .build();
            } catch (Exception e) {
                log.error("Failed to send OTP, proceeding without 2FA: {}", e.getMessage());
                // Fall through to generate token without OTP
            }
        }

        // Generate token directly if 2FA is disabled or OTP sending failed
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole(), // String role
                user.getId()
        );

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .requiresOTP(false)
                .message("Login successful!")
                .build();
    }

    @Transactional
    public AuthResponse verifyOTP(OTPRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify OTP
        otpService.verifyOTP(request.getEmail(), request.getCode());

        // Mark email as verified
        if (!user.getEmailVerified()) {
            user.setEmailVerified(true);
            userRepository.save(user);
        }

        // Generate JWT token
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole(), // String role
                user.getId()
        );

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .requiresOTP(false)
                .message("Verification successful!")
                .build();
    }

    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate and send OTP
        String otp = otpService.generateAndSendOTP(email);
        emailService.sendPasswordResetEmail(email, otp);

        log.info("Password reset requested for: {}", email);
    }

    @Transactional
    public AuthResponse resetPassword(PasswordResetRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify OTP
        otpService.verifyOTP(request.getEmail(), request.getOtp());

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        log.info("Password reset successful for: {}", request.getEmail());

        return AuthResponse.builder()
                .message("Password reset successful! Please login with your new password.")
                .build();
    }

    public void resendOTP(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new RuntimeException("User not found");
        }
        otpService.generateAndSendOTP(email);
    }

    @Transactional
    public AuthResponse customerSignup(CustomerSignupRequest request) {
        // Validate email uniqueness
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create customer
        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .build();

        customer = customerRepository.save(customer);
        log.info("Customer created: {}", customer.getEmail());

        // Generate JWT token for customer
        String token = jwtService.generateToken(
                customer.getEmail(),
                "CUSTOMER",
                customer.getId()
        );

        return AuthResponse.builder()
                .token(token)
                .userId(customer.getId())
                .email(customer.getEmail())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .role("CUSTOMER")
                .requiresOTP(false)
                .message("Registration successful!")
                .build();
    }

    public AuthResponse customerLogin(LoginRequest request) {
        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        // Generate JWT token
        String token = jwtService.generateToken(
                customer.getEmail(),
                "CUSTOMER",
                customer.getId()
        );

        return AuthResponse.builder()
                .token(token)
                .userId(customer.getId())
                .email(customer.getEmail())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .role("CUSTOMER")
                .requiresOTP(false)
                .message("Login successful!")
                .build();
    }
}