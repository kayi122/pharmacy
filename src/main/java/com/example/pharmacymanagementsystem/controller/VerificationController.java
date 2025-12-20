package com.example.pharmacymanagementsystem.controller;

import com.example.pharmacymanagementsystem.service.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
        Map<String, String> response = new HashMap<>();
        response.put("message", "OTP sent successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        
        try {
            boolean verified = otpService.verifyOTP(email, code);
            Map<String, Boolean> response = new HashMap<>();
            response.put("verified", verified);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        otpService.generateAndSendVerificationCode(email);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Verification code sent successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-verification-code")
    public ResponseEntity<?> verifyVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        
        try {
            boolean verified = otpService.verifyVerificationCode(email, code);
            Map<String, Boolean> response = new HashMap<>();
            response.put("verified", verified);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/send-2fa-code")
    public ResponseEntity<?> sendTwoFactorCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        otpService.generateAndSendTwoFactorCode(email);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Two-factor authentication code sent successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-2fa-code")
    public ResponseEntity<?> verifyTwoFactorCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        
        try {
            boolean verified = otpService.verifyTwoFactorCode(email, code);
            Map<String, Boolean> response = new HashMap<>();
            response.put("verified", verified);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}