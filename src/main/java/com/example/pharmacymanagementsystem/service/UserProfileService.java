package com.example.pharmacymanagementsystem.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pharmacymanagementsystem.dto.UserProfileDTO;
import com.example.pharmacymanagementsystem.exception.DuplicateResourceException;
import com.example.pharmacymanagementsystem.exception.ResourceNotFoundException;
import com.example.pharmacymanagementsystem.model.User;
import com.example.pharmacymanagementsystem.model.UserProfile;
import com.example.pharmacymanagementsystem.repository.UserProfileRepository;
import com.example.pharmacymanagementsystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    public List<UserProfileDTO> getAllProfiles() {
        log.info("Fetching all user profiles");
        return userProfileRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserProfileDTO> getAllProfilesWithUser() {
        log.info("Fetching all user profiles with user details");
        return userProfileRepository.findAllWithUser().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserProfileDTO getProfileById(Long id) {
        log.info("Fetching profile with ID: {}", id);
        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + id));
        return convertToDTO(profile);
    }

    public UserProfileDTO getProfileByIdWithUser(Long id) {
        log.info("Fetching profile with ID and user details: {}", id);
        UserProfile profile = userProfileRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + id));
        return convertToDTO(profile);
    }

    public UserProfileDTO getProfileByUserId(Long userId) {
        log.info("Fetching profile for user: {}", userId);
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user with id: " + userId));
        return convertToDTO(profile);
    }

    public UserProfileDTO getProfileByNationalId(String nationalId) {
        log.info("Fetching profile with national ID: {}", nationalId);
        UserProfile profile = userProfileRepository.findByNationalId(nationalId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with national ID: " + nationalId));
        return convertToDTO(profile);
    }

    public List<UserProfileDTO> getProfilesByDateOfBirth(LocalDate dateOfBirth) {
        log.info("Fetching profiles with date of birth: {}", dateOfBirth);
        return userProfileRepository.findByDateOfBirth(dateOfBirth).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserProfileDTO> getProfilesBornAfter(LocalDate date) {
        log.info("Fetching profiles born after: {}", date);
        return userProfileRepository.findByDateOfBirthAfter(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserProfileDTO> getProfilesBornBefore(LocalDate date) {
        log.info("Fetching profiles born before: {}", date);
        return userProfileRepository.findByDateOfBirthBefore(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserProfileDTO> getProfilesBornBetween(LocalDate startDate, LocalDate endDate) {
        log.info("Fetching profiles born between {} and {}", startDate, endDate);
        return userProfileRepository.findByDateOfBirthBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserProfileDTO> searchByBio(String keyword) {
        log.info("Searching profiles by bio: {}", keyword);
        return userProfileRepository.findByBioContainingIgnoreCase(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserProfileDTO> getProfilesWithPictures() {
        log.info("Fetching profiles with pictures");
        return userProfileRepository.findProfilesWithPictures().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserProfileDTO> getProfilesWithoutPictures() {
        log.info("Fetching profiles without pictures");
        return userProfileRepository.findProfilesWithoutPictures().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserProfileDTO> getProfilesByUserRole(String role) {
        log.info("Fetching profiles for users with role: {}", role);
        return userProfileRepository.findByUserRole(role).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserProfileDTO> getProfilesByUserLocation(Long locationId) {
        log.info("Fetching profiles for users in location: {}", locationId);
        return userProfileRepository.findByUserLocation(locationId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserProfileDTO createProfile(UserProfile profile) {
        log.info("Creating new profile for user: {}", profile.getUser().getId());

        // Check if user exists
        User user = userRepository.findById(profile.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check if profile already exists for this user
        if (userProfileRepository.existsByUserId(user.getId())) {
            throw new DuplicateResourceException("Profile already exists for user with id: " + user.getId());
        }

        // Check for duplicate national ID
        if (profile.getNationalId() != null && userProfileRepository.existsByNationalId(profile.getNationalId())) {
            throw new DuplicateResourceException("Profile with national ID " + profile.getNationalId() + " already exists");
        }

        profile.setUser(user);
        UserProfile savedProfile = userProfileRepository.save(profile);
        log.info("Profile created successfully with ID: {}", savedProfile.getId());
        return convertToDTO(savedProfile);
    }

    @Transactional
    public UserProfileDTO updateProfile(Long id, UserProfile updatedProfile) {
        log.info("Updating profile with ID: {}", id);

        UserProfile existingProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + id));

        // Check for duplicate national ID (excluding current profile)
        if (updatedProfile.getNationalId() != null
                && (existingProfile.getNationalId() == null || !existingProfile.getNationalId().equals(updatedProfile.getNationalId()))
                && userProfileRepository.existsByNationalId(updatedProfile.getNationalId())) {
            throw new DuplicateResourceException("Profile with national ID " + updatedProfile.getNationalId() + " already exists");
        }

        existingProfile.setDateOfBirth(updatedProfile.getDateOfBirth());
        existingProfile.setNationalId(updatedProfile.getNationalId());
        existingProfile.setBio(updatedProfile.getBio());
        existingProfile.setProfilePicture(updatedProfile.getProfilePicture());

        UserProfile savedProfile = userProfileRepository.save(existingProfile);
        log.info("Profile updated successfully with ID: {}", savedProfile.getId());
        return convertToDTO(savedProfile);
    }

    @Transactional
    public void deleteProfile(Long id) {
        log.info("Deleting profile with ID: {}", id);

        if (!userProfileRepository.existsById(id)) {
            throw new ResourceNotFoundException("Profile not found with id: " + id);
        }

        userProfileRepository.deleteById(id);
        log.info("Profile deleted successfully with ID: {}", id);
    }

    public boolean existsByNationalId(String nationalId) {
        return userProfileRepository.existsByNationalId(nationalId);
    }

    public boolean existsByUserId(Long userId) {
        return userProfileRepository.existsByUserId(userId);
    }

    public long countAllProfiles() {
        return userProfileRepository.count();
    }

    public long countByAgeRange(Integer minAge, Integer maxAge) {
        return userProfileRepository.countByAgeRange(minAge, maxAge);
    }

    private UserProfileDTO convertToDTO(UserProfile profile) {
        return new UserProfileDTO(profile);
    }
}
