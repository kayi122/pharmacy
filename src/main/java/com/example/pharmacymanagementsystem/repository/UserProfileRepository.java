package com.example.pharmacymanagementsystem.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pharmacymanagementsystem.model.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    // Find profile by user ID
    Optional<UserProfile> findByUserId(Long userId);

    // Find profile by national ID
    Optional<UserProfile> findByNationalId(String nationalId);

    // Check if profile exists by national ID
    Boolean existsByNationalId(String nationalId);

    // Check if profile exists by user ID
    Boolean existsByUserId(Long userId);

    // Find profiles by date of birth
    List<UserProfile> findByDateOfBirth(LocalDate dateOfBirth);

    // Find profiles born after a specific date
    List<UserProfile> findByDateOfBirthAfter(LocalDate date);

    // Find profiles born before a specific date
    List<UserProfile> findByDateOfBirthBefore(LocalDate date);

    // Find profiles born between dates
    List<UserProfile> findByDateOfBirthBetween(LocalDate startDate, LocalDate endDate);

    // Search profiles by bio
    List<UserProfile> findByBioContainingIgnoreCase(String keyword);

    // Find all profiles with user details
    @Query("SELECT p FROM UserProfile p LEFT JOIN FETCH p.user")
    List<UserProfile> findAllWithUser();

    // Find profile by ID with user details
    @Query("SELECT p FROM UserProfile p LEFT JOIN FETCH p.user WHERE p.id = :profileId")
    Optional<UserProfile> findByIdWithUser(@Param("profileId") Long profileId);

    // Find profiles with profile pictures
    @Query("SELECT p FROM UserProfile p WHERE p.profilePicture IS NOT NULL AND p.profilePicture != ''")
    List<UserProfile> findProfilesWithPictures();

    // Find profiles without profile pictures
    @Query("SELECT p FROM UserProfile p WHERE p.profilePicture IS NULL OR p.profilePicture = ''")
    List<UserProfile> findProfilesWithoutPictures();

    // Count profiles by age range (calculated)
    @Query("SELECT COUNT(p) FROM UserProfile p "
            + "WHERE YEAR(CURRENT_DATE) - YEAR(p.dateOfBirth) BETWEEN :minAge AND :maxAge")
    Long countByAgeRange(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);

    // Find profiles by user role
    @Query("SELECT p FROM UserProfile p JOIN p.user u WHERE u.role = :role")
    List<UserProfile> findByUserRole(@Param("role") String role);

    // Find profiles by user location
    @Query("SELECT p FROM UserProfile p JOIN p.user u WHERE u.location.id = :locationId")
    List<UserProfile> findByUserLocation(@Param("locationId") Long locationId);
}
