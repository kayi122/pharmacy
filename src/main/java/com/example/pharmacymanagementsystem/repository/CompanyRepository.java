package com.example.pharmacymanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pharmacymanagementsystem.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    // Check if company exists by email
    Boolean existsByEmail(String email);

    // Check if company exists by registration number
    Boolean existsByRegistrationNumber(String registrationNumber);

    // Find company by email
    Optional<Company> findByEmail(String email);

    // Find company by registration number
    Optional<Company> findByRegistrationNumber(String registrationNumber);

    // Find company by name
    Optional<Company> findByName(String name);

    // Find companies by name (case insensitive, partial match)
    List<Company> findByNameContainingIgnoreCase(String name);

    // Find companies by country
    List<Company> findByCountry(String country);

    // Find companies by country (case insensitive)
    List<Company> findByCountryIgnoreCase(String country);

    // Find companies by phone
    List<Company> findByPhoneContaining(String phone);

    // Search companies by name or country
    @Query("SELECT c FROM Company c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "OR LOWER(c.country) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Company> searchByNameOrCountry(@Param("keyword") String keyword);

    // Find all companies with medicines
    @Query("SELECT DISTINCT c FROM Company c LEFT JOIN FETCH c.medicines")
    List<Company> findAllWithMedicines();

    // Find company by ID with medicines
    @Query("SELECT c FROM Company c LEFT JOIN FETCH c.medicines WHERE c.id = :companyId")
    Optional<Company> findByIdWithMedicines(@Param("companyId") Long companyId);

    // Find companies that supply a specific medicine category
    @Query("SELECT DISTINCT c FROM Company c JOIN c.medicines m WHERE m.category = :category")
    List<Company> findByMedicineCategory(@Param("category") String category);

    // Count medicines per company
    @Query("SELECT COUNT(m) FROM Company c JOIN c.medicines m WHERE c.id = :companyId")
    Long countMedicinesByCompany(@Param("companyId") Long companyId);

    // Check if company has medicines
    @Query("SELECT COUNT(m) > 0 FROM Company c JOIN c.medicines m WHERE c.id = :companyId")
    Boolean hasMedicines(@Param("companyId") Long companyId);

    // Find companies ordered by name
    List<Company> findAllByOrderByNameAsc();

    // Find companies by country ordered by name
    List<Company> findByCountryOrderByNameAsc(String country);

    // Get total value of medicines for a company
    @Query("SELECT COALESCE(SUM(m.purchasePrice * m.quantity), 0) FROM Company c "
            + "JOIN c.medicines m WHERE c.id = :companyId")
    Double getTotalMedicineValueByCompany(@Param("companyId") Long companyId);

    // Count companies by country
    Long countByCountry(String country);

    // Find companies with expired medicines
    @Query("SELECT DISTINCT c FROM Company c JOIN c.medicines m WHERE m.isExpired = true")
    List<Company> findCompaniesWithExpiredMedicines();

    // Find companies with low stock medicines
    @Query("SELECT DISTINCT c FROM Company c JOIN c.medicines m WHERE m.quantity < :threshold")
    List<Company> findCompaniesWithLowStockMedicines(@Param("threshold") Integer threshold);
}
