package com.example.pharmacymanagementsystem.dto;

import java.time.LocalDate;

import com.example.pharmacymanagementsystem.model.UserProfile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {

    private Long id;
    private LocalDate dateOfBirth;
    private String nationalId;
    private String bio;
    private String profilePicture;

    // User information
    private Long userId;
    private String userFullName;
    private String userEmail;

    // Calculated field
    private Integer age;

    public UserProfileDTO(UserProfile profile) {
        this.id = profile.getId();
        this.dateOfBirth = profile.getDateOfBirth();
        this.nationalId = profile.getNationalId();
        this.bio = profile.getBio();
        this.profilePicture = profile.getProfilePicture();

        if (profile.getUser() != null) {
            this.userId = profile.getUser().getId();
            this.userFullName = profile.getUser().getFirstName() + " " + profile.getUser().getLastName();
            this.userEmail = profile.getUser().getEmail();
        }

        // Calculate age
        if (this.dateOfBirth != null) {
            this.age = LocalDate.now().getYear() - this.dateOfBirth.getYear();
        }
    }
}
