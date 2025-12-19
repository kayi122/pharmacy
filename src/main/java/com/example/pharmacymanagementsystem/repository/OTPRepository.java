package com.example.pharmacymanagementsystem.repository;

import com.example.pharmacymanagementsystem.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {

    Optional<OTP> findByEmailAndCodeAndVerifiedFalse(String email, String code);

    @Query("SELECT o FROM OTP o WHERE o.email = ?1 AND o.verified = false AND o.expiryTime > ?2 ORDER BY o.createdAt DESC")
    Optional<OTP> findLatestValidOTP(String email, LocalDateTime now);

    void deleteByEmail(String email);

    void deleteByExpiryTimeBefore(LocalDateTime time);
}