package com.example.pharmacymanagementsystem.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pharmacymanagementsystem.model.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    // ==================== EXISTING QUERIES (List) ====================
    
    // Find sales by medicine
    List<Sale> findByMedicineId(Long medicineId);

    // Find sales by user
    List<Sale> findByUserId(Long userId);

    // Find sales by customer name
    List<Sale> findByCustomerNameContainingIgnoreCase(String customerName);

    // Find sales by customer phone
    List<Sale> findByCustomerPhone(String customerPhone);

    // Find sales by date range
    List<Sale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Find sales after a specific date
    List<Sale> findBySaleDateAfter(LocalDateTime date);

    // Find sales before a specific date
    List<Sale> findBySaleDateBefore(LocalDateTime date);

    // Find sales ordered by date (newest first)
    List<Sale> findAllByOrderBySaleDateDesc();

    // Find sales ordered by total price (highest first)
    List<Sale> findAllByOrderByTotalPriceDesc();

    // Find all sales with medicine and user details
    @Query("SELECT s FROM Sale s "
            + "LEFT JOIN FETCH s.medicine "
            + "LEFT JOIN FETCH s.user")
    List<Sale> findAllWithDetails();

    // Find sales by medicine with details
    @Query("SELECT s FROM Sale s "
            + "LEFT JOIN FETCH s.medicine m "
            + "LEFT JOIN FETCH s.user "
            + "WHERE m.id = :medicineId")
    List<Sale> findByMedicineIdWithDetails(@Param("medicineId") Long medicineId);

    // Find sales by user with details
    @Query("SELECT s FROM Sale s "
            + "LEFT JOIN FETCH s.medicine "
            + "LEFT JOIN FETCH s.user u "
            + "WHERE u.id = :userId")
    List<Sale> findByUserIdWithDetails(@Param("userId") Long userId);

    // Get total sales count by medicine
    Long countByMedicineId(Long medicineId);

    // Get total sales count by user
    Long countByUserId(Long userId);

    // Get total revenue
    @Query("SELECT COALESCE(SUM(s.totalPrice), 0) FROM Sale s")
    Double getTotalRevenue();

    // Get total revenue by medicine
    @Query("SELECT COALESCE(SUM(s.totalPrice), 0) FROM Sale s WHERE s.medicine.id = :medicineId")
    Double getTotalRevenueByMedicine(@Param("medicineId") Long medicineId);

    // Get total revenue by user
    @Query("SELECT COALESCE(SUM(s.totalPrice), 0) FROM Sale s WHERE s.user.id = :userId")
    Double getTotalRevenueByUser(@Param("userId") Long userId);

    // Get total revenue by date range
    @Query("SELECT COALESCE(SUM(s.totalPrice), 0) FROM Sale s "
            + "WHERE s.saleDate BETWEEN :startDate AND :endDate")
    Double getTotalRevenueByDateRange(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Get total quantity sold
    @Query("SELECT COALESCE(SUM(s.quantity), 0) FROM Sale s")
    Long getTotalQuantitySold();

    // Get total quantity sold by medicine
    @Query("SELECT COALESCE(SUM(s.quantity), 0) FROM Sale s WHERE s.medicine.id = :medicineId")
    Long getTotalQuantitySoldByMedicine(@Param("medicineId") Long medicineId);

    // FIXED: Find today's sales using CAST
    @Query("SELECT s FROM Sale s WHERE CAST(s.saleDate AS DATE) = CURRENT_DATE ORDER BY s.saleDate DESC")
    List<Sale> findTodaySales();

    // FIXED: Get today's revenue using CAST
    @Query("SELECT COALESCE(SUM(s.totalPrice), 0) FROM Sale s WHERE CAST(s.saleDate AS DATE) = CURRENT_DATE")
    Double getTodayRevenue();

    // Find this week's sales
    @Query("SELECT s FROM Sale s WHERE s.saleDate >= :weekStart ORDER BY s.saleDate DESC")
    List<Sale> findWeeklySales(@Param("weekStart") LocalDateTime weekStart);

    // Find this month's sales
    @Query("SELECT s FROM Sale s WHERE EXTRACT(MONTH FROM s.saleDate) = :month AND EXTRACT(YEAR FROM s.saleDate) = :year "
            + "ORDER BY s.saleDate DESC")
    List<Sale> findMonthlySales(@Param("month") int month, @Param("year") int year);

    // Get top selling medicines
    @Query("SELECT s.medicine.id, s.medicine.name, SUM(s.quantity) as totalQuantity "
            + "FROM Sale s GROUP BY s.medicine.id, s.medicine.name "
            + "ORDER BY totalQuantity DESC")
    List<Object[]> findTopSellingMedicines();

    // Get sales statistics by medicine category
    @Query("SELECT m.category, COUNT(s), SUM(s.totalPrice) "
            + "FROM Sale s JOIN s.medicine m "
            + "GROUP BY m.category")
    List<Object[]> getSalesStatisticsByCategory();

    // Get sales count by date range
    @Query("SELECT COUNT(s) FROM Sale s WHERE s.saleDate BETWEEN :startDate AND :endDate")
    Long countSalesByDateRange(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Find high value sales (above threshold)
    @Query("SELECT s FROM Sale s WHERE s.totalPrice >= :threshold ORDER BY s.totalPrice DESC")
    List<Sale> findHighValueSales(@Param("threshold") Double threshold);

    // Get average sale value
    @Query("SELECT AVG(s.totalPrice) FROM Sale s")
    Double getAverageSaleValue();

    // Get average sale value by medicine
    @Query("SELECT AVG(s.totalPrice) FROM Sale s WHERE s.medicine.id = :medicineId")
    Double getAverageSaleValueByMedicine(@Param("medicineId") Long medicineId);

    // ==================== NEW: PAGINATION SUPPORT ====================
    
    // Paginated sales
    Page<Sale> findAll(Pageable pageable);
    
    // Paginated sales by medicine
    Page<Sale> findByMedicineId(Long medicineId, Pageable pageable);
    
    // Paginated sales by user
    Page<Sale> findByUserId(Long userId, Pageable pageable);
    
    // Paginated sales by customer name search
    Page<Sale> findByCustomerNameContainingIgnoreCase(String customerName, Pageable pageable);
    
    // Paginated sales by date range
    Page<Sale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // Paginated sales with details
    @Query("SELECT s FROM Sale s LEFT JOIN FETCH s.medicine LEFT JOIN FETCH s.user")
    Page<Sale> findAllWithDetails(Pageable pageable);
    
    // Global search in sales (for search bar)
    @Query("SELECT s FROM Sale s WHERE " +
           "LOWER(s.customerName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.customerPhone) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.medicine.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.user.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Sale> searchSales(@Param("search") String search, Pageable pageable);
}