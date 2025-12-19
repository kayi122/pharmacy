// package com.example.pharmacymanagementsystem.repository;

// import com.example.pharmacymanagementsystem.model.User;
// import com.example.pharmacymanagementsystem.model.User.Role;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
// import org.springframework.stereotype.Repository;

// import java.util.List;
// import java.util.Optional;

// @Repository
// public interface UserRepository extends JpaRepository<User, Long> {

//     // ========= LOOKUP FOR AUTH / LOGIN =========

//     // For UserDetailsService
//     Optional<User> findByEmail(String email);

//     // Only active + email verified (if you want stricter login)
//     Optional<User> findByEmailAndActiveTrueAndEmailVerifiedTrue(String email);

//     boolean existsByEmail(String email);

//     // ========= BASIC FILTERS =========

//     List<User> findByRole(Role role);

//     Page<User> findByRole(Role role, Pageable pageable);

//     List<User> findByActiveTrue();

//     Page<User> findByActiveTrue(Pageable pageable);

//     List<User> findByEmailVerifiedTrue();

//     Page<User> findByEmailVerifiedTrue(Pageable pageable);

//     // By location
//     List<User> findByLocationId(Long locationId);

//     Page<User> findByLocationId(Long locationId, Pageable pageable);

//     // ========= COUNTS / STATISTICS =========

//     Long countByRole(Role role);

//     Long countByActiveTrue();

//     Long countByEmailVerifiedTrue();

//     @Query("SELECT u.role, COUNT(u) FROM User u GROUP BY u.role")
//     List<Object[]> countUsersByRole();

//     // ========= SEARCH (for search bar) =========

//     @Query("""
//            SELECT u FROM User u
//            WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
//               OR LOWER(u.lastName)  LIKE LOWER(CONCAT('%', :search, '%'))
//               OR LOWER(u.email)     LIKE LOWER(CONCAT('%', :search, '%'))
//               OR LOWER(u.phone)     LIKE LOWER(CONCAT('%', :search, '%'))
//            """)
//     Page<User> searchUsers(@Param("search") String search, Pageable pageable);
// }
package com.example.pharmacymanagementsystem.repository;

import com.example.pharmacymanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // === Basic Lookups (Used by AuthService & UserService) ===
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    // === Search Methods (Used by UserService) ===
    List<User> findByFirstNameContainingIgnoreCase(String firstName);
    List<User> findByLastNameContainingIgnoreCase(String lastName);
    
    @Query("SELECT u FROM User u WHERE LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> searchByName(@Param("name") String name);

    // === Role & Location Filters (Using String for Role compatibility) ===
    List<User> findByRole(String role);
    List<User> findByRoleOrderByLastNameAscFirstNameAsc(String role);
    long countByRole(String role);
    List<User> findByLocationId(Long locationId);
    long countByLocationId(Long locationId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.location LEFT JOIN FETCH u.userProfile WHERE u.location.id = :locationId")
    List<User> findByLocationIdWithDetails(@Param("locationId") Long locationId);

    // === Relationship Fetching ===
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.location LEFT JOIN FETCH u.userProfile LEFT JOIN FETCH u.sales")
    List<User> findAllWithRelations();

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.location LEFT JOIN FETCH u.userProfile WHERE u.id = :id")
    Optional<User> findByIdWithRelations(@Param("id") Long id);

    List<User> findAllByOrderByLastNameAscFirstNameAsc();

    // === Custom Logic Checks ===
    @Query("SELECT u FROM User u LEFT JOIN u.userProfile p WHERE p IS NULL")
    List<User> findUsersWithoutProfile();

    @Query("SELECT DISTINCT u FROM User u JOIN u.sales s")
    List<User> findUsersWithSales();

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM UserProfile p WHERE p.user.id = :userId")
    boolean hasProfile(@Param("userId") Long userId);

    // === Statistics (Sales & Revenue) ===
    @Query("SELECT COUNT(s) FROM Sale s WHERE s.user.id = :userId")
    long countSalesByUser(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(s.totalPrice), 0) FROM Sale s WHERE s.user.id = :userId")
    Double getTotalRevenueByUser(@Param("userId") Long userId);

    @Query("SELECT u, COUNT(s) as sc FROM User u JOIN u.sales s GROUP BY u.id, u.firstName, u.lastName, u.email, u.phone, u.password, u.role, u.active, u.emailVerified, u.twoFactorEnabled, u.createdAt, u.updatedAt, u.location ORDER BY sc DESC")
    List<Object[]> findTopPerformingUsersBySalesCount();

    @Query("SELECT u, SUM(s.totalPrice) as rev FROM User u JOIN u.sales s GROUP BY u.id, u.firstName, u.lastName, u.email, u.phone, u.password, u.role, u.active, u.emailVerified, u.twoFactorEnabled, u.createdAt, u.updatedAt, u.location ORDER BY rev DESC")
    List<Object[]> findTopPerformingUsersByRevenue();
}