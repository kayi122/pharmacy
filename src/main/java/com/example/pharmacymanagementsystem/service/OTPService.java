package com.example.pharmacymanagementsystem.service;

import com.example.pharmacymanagementsystem.model.OTP;
import com.example.pharmacymanagementsystem.repository.OTPRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class OTPService {

    private final OTPRepository otpRepository;
    private final EmailService emailService;

    @Value("${otp.expiration}")
    private Long otpExpiration;

    public String generateAndSendOTP(String email) {
        // Generate 6-digit OTP
        String code = String.format("%06d", new Random().nextInt(999999));

        // Save OTP
        OTP otp = OTP.builder()
                .email(email)
                .code(code)
                .expiryTime(LocalDateTime.now().plusSeconds(otpExpiration / 1000))
                .verified(false)
                .createdAt(LocalDateTime.now())
                .build();

        otpRepository.save(otp);

        // Send email (handle failures gracefully)
        try {
            emailService.sendOTP(email, code);
            log.info("OTP generated and sent for email: {}", email);
        } catch (Exception e) {
            log.error("Failed to send OTP email, but OTP saved: {}", e.getMessage());
            log.warn("OTP {} generated for {} but email failed", code, email);
        }

        return code;
    }

    public String generateAndSendVerificationCode(String email) {
        // Generate 8-digit verification code
        String code = String.format("%08d", new Random().nextInt(99999999));

        // Save verification code with different prefix
        OTP verification = OTP.builder()
                .email(email + "_verification")
                .code(code)
                .expiryTime(LocalDateTime.now().plusSeconds(otpExpiration / 1000))
                .verified(false)
                .createdAt(LocalDateTime.now())
                .build();

        otpRepository.save(verification);

        // Send email
        emailService.sendVerificationCode(email, code);

        log.info("Verification code generated for email: {}", email);
        return code;
    }

    public String generateAndSendTwoFactorCode(String email) {
        // Generate 6-digit two-factor code
        String code = String.format("%06d", new Random().nextInt(999999));

        // Save two-factor code with different prefix
        OTP twoFactor = OTP.builder()
                .email(email + "_2fa")
                .code(code)
                .expiryTime(LocalDateTime.now().plusSeconds(otpExpiration / 1000))
                .verified(false)
                .createdAt(LocalDateTime.now())
                .build();

        otpRepository.save(twoFactor);

        // Send email
        emailService.sendTwoFactorCode(email, code);

        log.info("Two-factor code generated for email: {}", email);
        return code;
    }

    @Transactional
    public boolean verifyOTP(String email, String code) {
        OTP otp = otpRepository.findByEmailAndCodeAndVerifiedFalse(email, code)
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));

        if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        otp.setVerified(true);
        otpRepository.save(otp);

        log.info("OTP verified for email: {}", email);
        return true;
    }

    @Transactional
    public boolean verifyVerificationCode(String email, String code) {
        OTP verification = otpRepository.findByEmailAndCodeAndVerifiedFalse(email + "_verification", code)
                .orElseThrow(() -> new RuntimeException("Invalid verification code"));

        if (verification.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification code has expired");
        }

        verification.setVerified(true);
        otpRepository.save(verification);

        log.info("Verification code verified for email: {}", email);
        return true;
    }

    @Transactional
    public boolean verifyTwoFactorCode(String email, String code) {
        OTP twoFactor = otpRepository.findByEmailAndCodeAndVerifiedFalse(email + "_2fa", code)
                .orElseThrow(() -> new RuntimeException("Invalid two-factor code"));

        if (twoFactor.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Two-factor code has expired");
        }

        twoFactor.setVerified(true);
        otpRepository.save(twoFactor);

        log.info("Two-factor code verified for email: {}", email);
        return true;
    }

    @Transactional
    public void cleanupExpiredOTPs() {
        otpRepository.deleteByExpiryTimeBefore(LocalDateTime.now());
    }
}