package com.example.pharmacymanagementsystem.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashUtil {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Hash common passwords
        System.out.println("admin123 -> " + encoder.encode("admin123"));
        System.out.println("password -> " + encoder.encode("password"));
        System.out.println("123456 -> " + encoder.encode("123456"));
        
        // Add more passwords as needed
        if (args.length > 0) {
            for (String password : args) {
                System.out.println(password + " -> " + encoder.encode(password));
            }
        }
    }
}