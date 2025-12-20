package com.example.pharmacymanagementsystem.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pharmacymanagementsystem.model.Medicine;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    // Check if medicine exists by name
    Boolean existsByName(String name);

    // Find medicine by name
    Optional<Medicine> findByName(String name);

    // Find medicines by name (case insensitive, partial match)
    List<Medicine> findByNameContainingIgnoreCase(String name);

    // Find medicines by category
    List<Medicine> findByCategory(String category);

    // Find medicines by category (case insensitive)
    List<Medicine> findByCategoryIgnoreCase(String category);

    // Find medicines by batch number
    List<Medicine> findByBatchNumber(String batchNumber);

    // Find medicines by company
    List<Medicine> findByCompanyId(Long companyId);

    // Find expired medicines
    List<Medicine> findByIsExpiredTrue();

    // Find non-expired medicines
    List<Medicine> findByIsExpiredFalse();

    // Find medicines expiring before a certain date
    List<Medicine> findByExpiryDateBefore(LocalDate date);

    // Find medicines expiring after a certain date
    List<Medicine> findByExpiryDateAfter(LocalDate date);

    // Find medicines expiring between dates
    List<Medicine> findByExpiryDateBetween(LocalDate startDate, LocalDate endDate);

    // Find medicines with low stock
    @Query("SELECT m FROM Medicine m WHERE m.quantity <= :threshold")
    List<Medicine> findLowStockMedicines(@Param("threshold") Integer threshold);

    // Find medicines with quantity less than specified
    List<Medicine> findByQuantityLessThan(Integer quantity);

    // Find medicines with quantity greater than specified
    List<Medicine> findByQuantityGreaterThan(Integer quantity);

    // Find medicines by price range
    @Query("SELECT m FROM Medicine m WHERE m.sellingPrice BETWEEN :minPrice AND :maxPrice")
    List<Medicine> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    // Search medicines by name or category
    @Query("SELECT m FROM Medicine m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "OR LOWER(m.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Medicine> searchByNameOrCategory(@Param("keyword") String keyword);

    // Find all medicines with company details
    @Query("SELECT m FROM Medicine m LEFT JOIN FETCH m.company")
    List<Medicine> findAllWithCompany();

    // Find all medicines with all relationships (fixed to avoid MultipleBagFetchException)
    @Query("SELECT DISTINCT m FROM Medicine m "
            + "LEFT JOIN FETCH m.company")
    List<Medicine> findAllWithRelations();

    // Find medicine by ID with all relationships (fixed to avoid MultipleBagFetchException)
    @Query("SELECT m FROM Medicine m "
            + "LEFT JOIN FETCH m.company "
            + "WHERE m.id = :medicineId")
    Optional<Medicine> findByIdWithRelations(@Param("medicineId") Long medicineId);

    // Find medicines by company with details
    @Query("SELECT m FROM Medicine m "
            + "LEFT JOIN FETCH m.company c "
            + "WHERE c.id = :companyId")
    List<Medicine> findByCompanyIdWithDetails(@Param("companyId") Long companyId);

    // Find medicines expiring soon (within specified days)
    @Query("SELECT m FROM Medicine m WHERE m.expiryDate BETWEEN CURRENT_DATE AND :date "
            + "AND m.isExpired = false ORDER BY m.expiryDate ASC")
    List<Medicine> findExpiringSoon(@Param("date") LocalDate date);

    // Count medicines by category
    Long countByCategory(String category);

    // Count medicines by company
    Long countByCompanyId(Long companyId);

    // Count expired medicines
    Long countByIsExpiredTrue();

    // Count low stock medicines
    @Query("SELECT COUNT(m) FROM Medicine m WHERE m.quantity <= :threshold")
    Long countLowStockMedicines(@Param("threshold") Integer threshold);

    // Get total inventory value
    @Query("SELECT COALESCE(SUM(m.purchasePrice * m.quantity), 0) FROM Medicine m")
    Double getTotalInventoryValue();

    // Get total inventory value by category
    @Query("SELECT COALESCE(SUM(m.purchasePrice * m.quantity), 0) FROM Medicine m WHERE m.category = :category")
    Double getInventoryValueByCategory(@Param("category") String category);

    // Get total inventory value by company
    @Query("SELECT COALESCE(SUM(m.purchasePrice * m.quantity), 0) FROM Medicine m WHERE m.company.id = :companyId")
    Double getInventoryValueByCompany(@Param("companyId") Long companyId);

    // Find medicines ordered by name
    List<Medicine> findAllByOrderByNameAsc();

    // Find medicines ordered by expiry date
    List<Medicine> findAllByOrderByExpiryDateAsc();

    // Find medicines ordered by quantity (lowest first)
    List<Medicine> findAllByOrderByQuantityAsc();

    // Find medicines entered after a specific date
    List<Medicine> findByEntryDateAfter(LocalDate date);

    // Find medicines manufactured after a specific date
    List<Medicine> findByManufactureDateAfter(LocalDate date);

    // Find medicines with agents
    @Query("SELECT DISTINCT m FROM Medicine m LEFT JOIN FETCH m.agents WHERE SIZE(m.agents) > 0")
    List<Medicine> findAllMedicinesWithAgents();

    // Count total sales for a medicine
    @Query("SELECT COUNT(s) FROM Medicine m JOIN m.sales s WHERE m.id = :medicineId")
    Long countSalesByMedicine(@Param("medicineId") Long medicineId);

    // Get total revenue for a medicine
    @Query("SELECT COALESCE(SUM(s.totalPrice), 0) FROM Medicine m JOIN m.sales s WHERE m.id = :medicineId")
    Double getTotalRevenueByMedicine(@Param("medicineId") Long medicineId);

    // Find medicines by agent
    @Query("SELECT m FROM Medicine m JOIN m.agents a WHERE a.id = :agentId")
    List<Medicine> findByAgentId(@Param("agentId") Long agentId);
}
