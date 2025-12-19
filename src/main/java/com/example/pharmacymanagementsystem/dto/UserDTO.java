package com.example.pharmacymanagementsystem.dto;

import com.example.pharmacymanagementsystem.model.User;
import com.example.pharmacymanagementsystem.util.LocationHelper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String role;

    // Location information
    private Long locationId;
    private String locationProvince;
    private String locationDistrict;
    private String locationSector;
    private String locationCell;
    private String locationVillage;

    // User status fields
    private Boolean active;
    private Boolean emailVerified;

    // User Profile information
    private UserProfileDTO userProfile;

    // Sales statistics
    private Long totalSalesCount;
    private Double totalSalesRevenue;

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.role = user.getRole();

        // Location info
        if (user.getLocation() != null) {
            this.locationId = user.getLocation().getId();
            this.locationProvince = LocationHelper.getProvince(user.getLocation());
            this.locationDistrict = LocationHelper.getDistrict(user.getLocation());
            this.locationSector = LocationHelper.getSector(user.getLocation());
            this.locationCell = LocationHelper.getCell(user.getLocation());
            this.locationVillage = LocationHelper.getVillage(user.getLocation());
        }

        // User status
        this.active = user.getActive();
        this.emailVerified = user.getEmailVerified();

        // User profile
        if (user.getUserProfile() != null) {
            this.userProfile = new UserProfileDTO(user.getUserProfile());
        }
    }
}
