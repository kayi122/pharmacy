package com.example.pharmacymanagementsystem.controller;

import com.example.pharmacymanagementsystem.service.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/verification")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VerificationController {

    private final OTPService otpService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        otpService.generateAndSendOTP(email);
        return ResponseEntity.ok(Map.of("message", "OTP sent successfully"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        
        try {
            boolean verified = otpService.verifyOTP(email, code);
            return ResponseEntity.ok(Map.of("verified", verified));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        otpService.generateAndSendVerificationCode(email);
        return ResponseEntity.ok(Map.of("message", "Verification code sent successfully"));
    }

    @PostMapping("/verify-verification-code")
    public ResponseEntity<?> verifyVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        
        try {
            boolean verified = otpService.verifyVerificationCode(email, code);
            return ResponseEntity.ok(Map.of("verified", verified));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/send-2fa-code")
    public ResponseEntity<?> sendTwoFactorCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        otpService.generateAndSendTwoFactorCode(email);
        return ResponseEntity.ok(Map.of("message", "Two-factor authentication code sent successfully"));
    }

    @PostMapping("/verify-2fa-code")
    public ResponseEntity<?> verifyTwoFactorCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        
        try {
            boolean verified = otpService.verifyTwoFactorCode(email, code);
            return ResponseEntity.ok(Map.of("verified", verified));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}