package com.example.pharmacymanagementsystem.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    // Password - write only for security
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false)
    private String role;

    // FIXED: Set to nullable = true to avoid PSQLException with existing data
    @Column(nullable = true)
    @Builder.Default
    private Boolean active = true;

    @Column(nullable = true)
    @Builder.Default
    private Boolean emailVerified = false;

    @Column(nullable = true)
    @Builder.Default
    private Boolean twoFactorEnabled = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    @JsonIgnoreProperties({ "users", "parent", "children", "hibernateLazyInitializer", "handler" })
    private Location location;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private UserProfile userProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Sale> sales;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (active == null)
            active = true;
        if (emailVerified == null)
            emailVerified = false;
        if (twoFactorEnabled == null)
            twoFactorEnabled = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // UserDetails implementation
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return active != null && active;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return (active != null && active) && (emailVerified != null && emailVerified);
    }

    @JsonIgnore
    public Role getRoleEnum() {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (Exception e) {
            return Role.CASHIER;
        }
    }

    public enum Role {
        ADMIN,
        PHARMACIST,
        CASHIER,
        INVENTORY_MANAGER,
        DOCTOR
    }
}