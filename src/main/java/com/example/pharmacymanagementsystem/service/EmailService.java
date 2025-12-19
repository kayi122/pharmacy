package com.example.pharmacymanagementsystem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOTP(String to, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Your OTP Code - Pharmacy Management System");
            message.setText(
                    "Your OTP code is: " + otp + "\n\n" +
                            "This code will expire in 5 minutes.\n\n" +
                            "If you didn't request this code, please ignore this email.\n\n" +
                            "Best regards,\n" +
                            "Pharmacy Management System");

            mailSender.send(message);
            log.info("OTP sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send OTP email: {}", e.getMessage());
            // Don't throw exception to prevent breaking the signup process
            log.warn("Email service unavailable, OTP: {} for user: {}", otp, to);
        }
    }

    public void sendWelcomeEmail(String to, String name) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Welcome to Pharmacy Management System");
            message.setText(
                    "Hello " + name + ",\n\n" +
                            "Welcome to our Pharmacy Management System!\n\n" +
                            "Your account has been successfully created.\n\n" +
                            "Best regards,\n" +
                            "Pharmacy Management System Team");

            mailSender.send(message);
            log.info("Welcome email sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send welcome email: {}", e.getMessage());
        }
    }

    public void sendPasswordResetEmail(String to, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Password Reset Request - Pharmacy Management System");
            message.setText(
                    "You requested to reset your password.\n\n" +
                            "Your password reset code is: " + otp + "\n\n" +
                            "This code will expire in 5 minutes.\n\n" +
                            "If you didn't request this, please ignore this email.\n\n" +
                            "Best regards,\n" +
                            "Pharmacy Management System");

            mailSender.send(message);
            log.info("Password reset email sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send password reset email: {}", e.getMessage());
            log.warn("Email service unavailable, password reset OTP: {} for user: {}", otp, to);
        }
    }

    public void sendVerificationCode(String to, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Account Verification Code - Pharmacy Management System");
            message.setText(
                    "Your account verification code is: " + code + "\n\n" +
                            "This code will expire in 5 minutes.\n\n" +
                            "Please use this code to verify your account.\n\n" +
                            "If you didn't request this code, please ignore this email.\n\n" +
                            "Best regards,\n" +
                            "Pharmacy Management System");

            mailSender.send(message);
            log.info("Verification code sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send verification code email: {}", e.getMessage());
            log.warn("Email service unavailable, verification code: {} for user: {}", code, to);
        }
    }

    public void sendTwoFactorCode(String to, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Two-Factor Authentication Code - Pharmacy Management System");
            message.setText(
                    "Your two-factor authentication code is: " + code + "\n\n" +
                            "This code will expire in 5 minutes.\n\n" +
                            "Please use this code to complete your login.\n\n" +
                            "If you didn't request this code, please secure your account immediately.\n\n" +
                            "Best regards,\n" +
                            "Pharmacy Management System");

            mailSender.send(message);
            log.info("Two-factor code sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send two-factor code email: {}", e.getMessage());
            log.warn("Email service unavailable, two-factor code: {} for user: {}", code, to);
        }
    }
}