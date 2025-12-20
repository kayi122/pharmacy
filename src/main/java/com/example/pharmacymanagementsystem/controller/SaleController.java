package com.example.pharmacymanagementsystem.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.example.pharmacymanagementsystem.dto.SaleDTO;
import com.example.pharmacymanagementsystem.model.Sale;
import com.example.pharmacymanagementsystem.service.SaleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@Slf4j
public class SaleController {

    private final SaleService saleService;

    @GetMapping("/paginated")
    public ResponseEntity<Map<String, Object>> getSalesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search) {
        log.info("GET request to fetch sales page {} with size {}", page, size);
        List<SaleDTO> allSales = saleService.getAllSalesWithDetails();
        
        if (search != null && !search.isEmpty()) {
            String searchLower = search.toLowerCase();
            allSales = allSales.stream()
                .filter(s -> (s.getCustomerName() != null && s.getCustomerName().toLowerCase().contains(searchLower)) ||
                            (s.getCustomerPhone() != null && s.getCustomerPhone().contains(search)) ||
                            (s.getMedicineName() != null && s.getMedicineName().toLowerCase().contains(searchLower)))
                .collect(java.util.stream.Collectors.toList());
        }
        
        int start = page * size;
        int end = Math.min(start + size, allSales.size());
        List<SaleDTO> pageSales = allSales.subList(start, end);
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", pageSales);
        response.put("currentPage", page);
        response.put("totalItems", allSales.size());
        response.put("totalPages", (int) Math.ceil((double) allSales.size() / size));
        
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SaleDTO>> getAllSales() {
        log.info("GET request to fetch all sales");
        List<SaleDTO> sales = saleService.getAllSales();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/with-details")
    public ResponseEntity<List<SaleDTO>> getAllSalesWithDetails() {
        log.info("GET request to fetch all sales with details");
        List<SaleDTO> sales = saleService.getAllSalesWithDetails();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> getSaleById(@PathVariable Long id) {
        log.info("GET request to fetch sale with ID: {}", id);
        SaleDTO sale = saleService.getSaleById(id);
        return ResponseEntity.ok(sale);
    }

    @GetMapping("/medicine/{medicineId}")
    public ResponseEntity<List<SaleDTO>> getSalesByMedicine(@PathVariable Long medicineId) {
        log.info("GET request to fetch sales for medicine: {}", medicineId);
        List<SaleDTO> sales = saleService.getSalesByMedicine(medicineId);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/medicine/{medicineId}/with-details")
    public ResponseEntity<List<SaleDTO>> getSalesByMedicineWithDetails(@PathVariable Long medicineId) {
        log.info("GET request to fetch sales for medicine with details: {}", medicineId);
        List<SaleDTO> sales = saleService.getSalesByMedicineWithDetails(medicineId);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SaleDTO>> getSalesByUser(@PathVariable Long userId) {
        log.info("GET request to fetch sales for user: {}", userId);
        List<SaleDTO> sales = saleService.getSalesByUser(userId);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/user/{userId}/with-details")
    public ResponseEntity<List<SaleDTO>> getSalesByUserWithDetails(@PathVariable Long userId) {
        log.info("GET request to fetch sales for user with details: {}", userId);
        List<SaleDTO> sales = saleService.getSalesByUserWithDetails(userId);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/search/customer-name")
    public ResponseEntity<List<SaleDTO>> searchByCustomerName(@RequestParam String customerName) {
        log.info("GET request to search sales by customer name: {}", customerName);
        List<SaleDTO> sales = saleService.searchByCustomerName(customerName);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/search/customer-phone")
    public ResponseEntity<List<SaleDTO>> getSalesByCustomerPhone(@RequestParam String customerPhone) {
        log.info("GET request to fetch sales by customer phone: {}", customerPhone);
        List<SaleDTO> sales = saleService.getSalesByCustomerPhone(customerPhone);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<SaleDTO>> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("GET request to fetch sales between {} and {}", startDate, endDate);
        List<SaleDTO> sales = saleService.getSalesByDateRange(startDate, endDate);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/after-date")
    public ResponseEntity<List<SaleDTO>> getSalesAfterDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        log.info("GET request to fetch sales after: {}", date);
        List<SaleDTO> sales = saleService.getSalesAfterDate(date);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/before-date")
    public ResponseEntity<List<SaleDTO>> getSalesBeforeDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        log.info("GET request to fetch sales before: {}", date);
        List<SaleDTO> sales = saleService.getSalesBeforeDate(date);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/ordered-by-date")
    public ResponseEntity<List<SaleDTO>> getAllSalesOrderedByDate() {
        log.info("GET request to fetch sales ordered by date");
        List<SaleDTO> sales = saleService.getAllSalesOrderedByDate();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/ordered-by-price")
    public ResponseEntity<List<SaleDTO>> getAllSalesOrderedByTotalPrice() {
        log.info("GET request to fetch sales ordered by total price");
        List<SaleDTO> sales = saleService.getAllSalesOrderedByTotalPrice();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/today")
    public ResponseEntity<List<SaleDTO>> getTodaySales() {
        log.info("GET request to fetch today's sales");
        List<SaleDTO> sales = saleService.getTodaySales();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/weekly")
    public ResponseEntity<List<SaleDTO>> getWeeklySales() {
        log.info("GET request to fetch this week's sales");
        List<SaleDTO> sales = saleService.getWeeklySales();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<SaleDTO>> getMonthlySales(
            @RequestParam int month,
            @RequestParam int year) {
        log.info("GET request to fetch sales for month {} of year {}", month, year);
        List<SaleDTO> sales = saleService.getMonthlySales(month, year);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/high-value")
    public ResponseEntity<List<SaleDTO>> getHighValueSales(
            @RequestParam(defaultValue = "1000.0") Double threshold) {
        log.info("GET request to fetch high value sales (threshold: {})", threshold);
        List<SaleDTO> sales = saleService.getHighValueSales(threshold);
        return ResponseEntity.ok(sales);
    }

    @PostMapping
    public ResponseEntity<SaleDTO> createSale(@RequestBody Sale sale) {
        log.info("POST request to create sale");
        SaleDTO createdSale = saleService.createSale(sale);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSale);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleDTO> updateSale(@PathVariable Long id, @RequestBody Sale sale) {
        log.info("PUT request to update sale with ID: {}", id);
        SaleDTO updatedSale = saleService.updateSale(id, sale);
        return ResponseEntity.ok(updatedSale);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        log.info("DELETE request to delete sale with ID: {}", id);
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countSales() {
        log.info("GET request to count all sales");
        long count = saleService.countAllSales();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/medicine/{medicineId}")
    public ResponseEntity<Long> countSalesByMedicine(@PathVariable Long medicineId) {
        log.info("GET request to count sales for medicine: {}", medicineId);
        long count = saleService.countSalesByMedicine(medicineId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/user/{userId}")
    public ResponseEntity<Long> countSalesByUser(@PathVariable Long userId) {
        log.info("GET request to count sales for user: {}", userId);
        long count = saleService.countSalesByUser(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/date-range")
    public ResponseEntity<Long> countSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("GET request to count sales between {} and {}", startDate, endDate);
        long count = saleService.countSalesByDateRange(startDate, endDate);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/revenue/total")
    public ResponseEntity<Double> getTotalRevenue() {
        log.info("GET request to get total revenue");
        Double revenue = saleService.getTotalRevenue();
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/revenue/medicine/{medicineId}")
    public ResponseEntity<Double> getTotalRevenueByMedicine(@PathVariable Long medicineId) {
        log.info("GET request to get total revenue for medicine: {}", medicineId);
        Double revenue = saleService.getTotalRevenueByMedicine(medicineId);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/revenue/user/{userId}")
    public ResponseEntity<Double> getTotalRevenueByUser(@PathVariable Long userId) {
        log.info("GET request to get total revenue for user: {}", userId);
        Double revenue = saleService.getTotalRevenueByUser(userId);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/revenue/date-range")
    public ResponseEntity<Double> getTotalRevenueByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("GET request to get revenue between {} and {}", startDate, endDate);
        Double revenue = saleService.getTotalRevenueByDateRange(startDate, endDate);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/revenue/today")
    public ResponseEntity<Double> getTodayRevenue() {
        log.info("GET request to get today's revenue");
        Double revenue = saleService.getTodayRevenue();
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/quantity/total")
    public ResponseEntity<Long> getTotalQuantitySold() {
        log.info("GET request to get total quantity sold");
        Long quantity = saleService.getTotalQuantitySold();
        return ResponseEntity.ok(quantity);
    }

    @GetMapping("/quantity/medicine/{medicineId}")
    public ResponseEntity<Long> getTotalQuantitySoldByMedicine(@PathVariable Long medicineId) {
        log.info("GET request to get total quantity sold for medicine: {}", medicineId);
        Long quantity = saleService.getTotalQuantitySoldByMedicine(medicineId);
        return ResponseEntity.ok(quantity);
    }

    @GetMapping("/average/value")
    public ResponseEntity<Double> getAverageSaleValue() {
        log.info("GET request to get average sale value");
        Double average = saleService.getAverageSaleValue();
        return ResponseEntity.ok(average);
    }

    @GetMapping("/average/value/medicine/{medicineId}")
    public ResponseEntity<Double> getAverageSaleValueByMedicine(@PathVariable Long medicineId) {
        log.info("GET request to get average sale value for medicine: {}", medicineId);
        Double average = saleService.getAverageSaleValueByMedicine(medicineId);
        return ResponseEntity.ok(average);
    }

    @GetMapping("/statistics/top-selling")
    public ResponseEntity<List<Object[]>> getTopSellingMedicines() {
        log.info("GET request to get top selling medicines");
        List<Object[]> statistics = saleService.getTopSellingMedicines();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/statistics/by-category")
    public ResponseEntity<List<Object[]>> getSalesStatisticsByCategory() {
        log.info("GET request to get sales statistics by category");
        List<Object[]> statistics = saleService.getSalesStatisticsByCategory();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getSalesStats() {
        log.info("GET request to get sales statistics");
        long totalSales = saleService.countAllSales();
        Double totalRevenue = saleService.getTotalRevenue();
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSales", totalSales);
        stats.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
        return ResponseEntity.ok(stats);
    }
}
