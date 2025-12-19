package com.example.pharmacymanagementsystem.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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

import com.example.pharmacymanagementsystem.dto.UserProfileDTO;
import com.example.pharmacymanagementsystem.model.UserProfile;
import com.example.pharmacymanagementsystem.service.UserProfileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/user-profiles")
@RequiredArgsConstructor
@Slf4j
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<List<UserProfileDTO>> getAllProfiles() {
        log.info("GET request to fetch all profiles");
        List<UserProfileDTO> profiles = userProfileService.getAllProfiles();
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/with-user")
    public ResponseEntity<List<UserProfileDTO>> getAllProfilesWithUser() {
        log.info("GET request to fetch all profiles with user details");
        List<UserProfileDTO> profiles = userProfileService.getAllProfilesWithUser();
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDTO> getProfileById(@PathVariable Long id) {
        log.info("GET request to fetch profile with ID: {}", id);
        UserProfileDTO profile = userProfileService.getProfileById(id);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/{id}/with-user")
    public ResponseEntity<UserProfileDTO> getProfileByIdWithUser(@PathVariable Long id) {
        log.info("GET request to fetch profile with ID and user details: {}", id);
        UserProfileDTO profile = userProfileService.getProfileByIdWithUser(id);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserProfileDTO> getProfileByUserId(@PathVariable Long userId) {
        log.info("GET request to fetch profile for user: {}", userId);
        UserProfileDTO profile = userProfileService.getProfileByUserId(userId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/national-id")
    public ResponseEntity<UserProfileDTO> getProfileByNationalId(@RequestParam String nationalId) {
        log.info("GET request to fetch profile with national ID: {}", nationalId);
        UserProfileDTO profile = userProfileService.getProfileByNationalId(nationalId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/dob")
    public ResponseEntity<List<UserProfileDTO>> getProfilesByDateOfBirth(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth) {
        log.info("GET request to fetch profiles with date of birth: {}", dateOfBirth);
        List<UserProfileDTO> profiles = userProfileService.getProfilesByDateOfBirth(dateOfBirth);
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/born-after")
    public ResponseEntity<List<UserProfileDTO>> getProfilesBornAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("GET request to fetch profiles born after: {}", date);
        List<UserProfileDTO> profiles = userProfileService.getProfilesBornAfter(date);
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/born-before")
    public ResponseEntity<List<UserProfileDTO>> getProfilesBornBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("GET request to fetch profiles born before: {}", date);
        List<UserProfileDTO> profiles = userProfileService.getProfilesBornBefore(date);
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/born-between")
    public ResponseEntity<List<UserProfileDTO>> getProfilesBornBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("GET request to fetch profiles born between {} and {}", startDate, endDate);
        List<UserProfileDTO> profiles = userProfileService.getProfilesBornBetween(startDate, endDate);
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/search/bio")
    public ResponseEntity<List<UserProfileDTO>> searchByBio(@RequestParam String keyword) {
        log.info("GET request to search profiles by bio: {}", keyword);
        List<UserProfileDTO> profiles = userProfileService.searchByBio(keyword);
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/with-pictures")
    public ResponseEntity<List<UserProfileDTO>> getProfilesWithPictures() {
        log.info("GET request to fetch profiles with pictures");
        List<UserProfileDTO> profiles = userProfileService.getProfilesWithPictures();
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/without-pictures")
    public ResponseEntity<List<UserProfileDTO>> getProfilesWithoutPictures() {
        log.info("GET request to fetch profiles without pictures");
        List<UserProfileDTO> profiles = userProfileService.getProfilesWithoutPictures();
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/user-role/{role}")
    public ResponseEntity<List<UserProfileDTO>> getProfilesByUserRole(@PathVariable String role) {
        log.info("GET request to fetch profiles for users with role: {}", role);
        List<UserProfileDTO> profiles = userProfileService.getProfilesByUserRole(role);
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/user-location/{locationId}")
    public ResponseEntity<List<UserProfileDTO>> getProfilesByUserLocation(@PathVariable Long locationId) {
        log.info("GET request to fetch profiles for users in location: {}", locationId);
        List<UserProfileDTO> profiles = userProfileService.getProfilesByUserLocation(locationId);
        return ResponseEntity.ok(profiles);
    }

    @PostMapping
    public ResponseEntity<UserProfileDTO> createProfile(@RequestBody UserProfile profile) {
        log.info("POST request to create profile for user: {}", profile.getUser().getId());
        UserProfileDTO createdProfile = userProfileService.createProfile(profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDTO> updateProfile(@PathVariable Long id, @RequestBody UserProfile profile) {
        log.info("PUT request to update profile with ID: {}", id);
        UserProfileDTO updatedProfile = userProfileService.updateProfile(id, profile);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        log.info("DELETE request to delete profile with ID: {}", id);
        userProfileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/national-id")
    public ResponseEntity<Boolean> checkNationalIdExists(@RequestParam String nationalId) {
        log.info("GET request to check if national ID exists: {}", nationalId);
        boolean exists = userProfileService.existsByNationalId(nationalId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/user/{userId}")
    public ResponseEntity<Boolean> checkProfileExistsByUserId(@PathVariable Long userId) {
        log.info("GET request to check if profile exists for user: {}", userId);
        boolean exists = userProfileService.existsByUserId(userId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countProfiles() {
        log.info("GET request to count all profiles");
        long count = userProfileService.countAllProfiles();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/age-range")
    public ResponseEntity<Long> countByAgeRange(
            @RequestParam Integer minAge,
            @RequestParam Integer maxAge) {
        log.info("GET request to count profiles by age range: {} - {}", minAge, maxAge);
        long count = userProfileService.countByAgeRange(minAge, maxAge);
        return ResponseEntity.ok(count);
    }
}
