package com.example.pharmacymanagementsystem.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pharmacymanagementsystem.dto.MedicineDTO;
import com.example.pharmacymanagementsystem.model.Medicine;
import com.example.pharmacymanagementsystem.service.MedicineService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
@Slf4j
public class MedicineController {

    private final MedicineService medicineService;

    @GetMapping
    public ResponseEntity<List<MedicineDTO>> getAllMedicines() {
        log.info("GET request to fetch all medicines");
        List<MedicineDTO> medicines = medicineService.getAllMedicines();
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Map<String, Object>> getMedicinesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        log.info("GET request to fetch medicines page {} with size {}", page, size);
        return ResponseEntity.ok(medicineService.getMedicinesPaginated(page, size, search));
    }

    @GetMapping("/with-relations")
    public ResponseEntity<List<MedicineDTO>> getAllMedicinesWithRelations() {
        log.info("GET request to fetch all medicines with relations");
        List<MedicineDTO> medicines = medicineService.getAllMedicinesWithRelations();
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineDTO> getMedicineById(@PathVariable Long id) {
        log.info("GET request to fetch medicine with ID: {}", id);
        MedicineDTO medicine = medicineService.getMedicineById(id);
        return ResponseEntity.ok(medicine);
    }

    @GetMapping("/{id}/with-relations")
    public ResponseEntity<MedicineDTO> getMedicineByIdWithRelations(@PathVariable Long id) {
        log.info("GET request to fetch medicine with ID and relationships: {}", id);
        MedicineDTO medicine = medicineService.getMedicineByIdWithRelations(id);
        return ResponseEntity.ok(medicine);
    }

    @GetMapping("/name")
    public ResponseEntity<MedicineDTO> getMedicineByName(@RequestParam String name) {
        log.info("GET request to fetch medicine with name: {}", name);
        MedicineDTO medicine = medicineService.getMedicineByName(name);
        return ResponseEntity.ok(medicine);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MedicineDTO>> searchByName(@RequestParam String name) {
        log.info("GET request to search medicines by name: {}", name);
        List<MedicineDTO> medicines = medicineService.searchByName(name);
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<MedicineDTO>> getMedicinesByCategory(@PathVariable String category) {
        log.info("GET request to fetch medicines in category: {}", category);
        List<MedicineDTO> medicines = medicineService.getMedicinesByCategory(category);
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/batch/{batchNumber}")
    public ResponseEntity<List<MedicineDTO>> getMedicinesByBatchNumber(@PathVariable String batchNumber) {
        log.info("GET request to fetch medicines with batch number: {}", batchNumber);
        List<MedicineDTO> medicines = medicineService.getMedicinesByBatchNumber(batchNumber);
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<MedicineDTO>> getMedicinesByCompany(@PathVariable Long companyId) {
        log.info("GET request to fetch medicines from company: {}", companyId);
        List<MedicineDTO> medicines = medicineService.getMedicinesByCompany(companyId);
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/company/{companyId}/with-details")
    public ResponseEntity<List<MedicineDTO>> getMedicinesByCompanyWithDetails(@PathVariable Long companyId) {
        log.info("GET request to fetch medicines from company with details: {}", companyId);
        List<MedicineDTO> medicines = medicineService.getMedicinesByCompanyWithDetails(companyId);
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/expired")
    public ResponseEntity<List<MedicineDTO>> getExpiredMedicines() {
        log.info("GET request to fetch expired medicines");
        List<MedicineDTO> medicines = medicineService.getExpiredMedicines();
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/non-expired")
    public ResponseEntity<List<MedicineDTO>> getNonExpiredMedicines() {
        log.info("GET request to fetch non-expired medicines");
        List<MedicineDTO> medicines = medicineService.getNonExpiredMedicines();
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/expiring-before")
    public ResponseEntity<List<MedicineDTO>> getMedicinesExpiringBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("GET request to fetch medicines expiring before: {}", date);
        List<MedicineDTO> medicines = medicineService.getMedicinesExpiringBefore(date);
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/expiring-between")
    public ResponseEntity<List<MedicineDTO>> getMedicinesExpiringBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("GET request to fetch medicines expiring between {} and {}", startDate, endDate);
        List<MedicineDTO> medicines = medicineService.getMedicinesExpiringBetween(startDate, endDate);
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/expiring-soon")
    public ResponseEntity<List<MedicineDTO>> getMedicinesExpiringSoon(@RequestParam(defaultValue = "30") Integer days) {
        log.info("GET request to fetch medicines expiring within {} days", days);
        List<MedicineDTO> medicines = medicineService.getMedicinesExpiringSoon(days);
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<MedicineDTO>> getLowStockMedicines(@RequestParam(defaultValue = "10") Integer threshold) {
        log.info("GET request to fetch low stock medicines (threshold: {})", threshold);
        List<MedicineDTO> medicines = medicineService.getLowStockMedicines(threshold);
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<MedicineDTO>> getMedicinesByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        log.info("GET request to fetch medicines in price range: {} - {}", minPrice, maxPrice);
        List<MedicineDTO> medicines = medicineService.getMedicinesByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/search/advanced")
    public ResponseEntity<List<MedicineDTO>> searchByNameOrCategory(@RequestParam String keyword) {
        log.info("GET request to search medicines by name or category: {}", keyword);
        List<MedicineDTO> medicines = medicineService.searchByNameOrCategory(keyword);
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/ordered-by-name")
    public ResponseEntity<List<MedicineDTO>> getAllMedicinesOrderedByName() {
        log.info("GET request to fetch medicines ordered by name");
        List<MedicineDTO> medicines = medicineService.getAllMedicinesOrderedByName();
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/ordered-by-expiry")
    public ResponseEntity<List<MedicineDTO>> getAllMedicinesOrderedByExpiryDate() {
        log.info("GET request to fetch medicines ordered by expiry date");
        List<MedicineDTO> medicines = medicineService.getAllMedicinesOrderedByExpiryDate();
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/ordered-by-quantity")
    public ResponseEntity<List<MedicineDTO>> getAllMedicinesOrderedByQuantity() {
        log.info("GET request to fetch medicines ordered by quantity");
        List<MedicineDTO> medicines = medicineService.getAllMedicinesOrderedByQuantity();
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<MedicineDTO>> getMedicinesByAgent(@PathVariable Long agentId) {
        log.info("GET request to fetch medicines for agent: {}", agentId);
        List<MedicineDTO> medicines = medicineService.getMedicinesByAgent(agentId);
        return ResponseEntity.ok(medicines);
    }

    @PostMapping
    public ResponseEntity<MedicineDTO> createMedicine(@RequestBody Medicine medicine) {
        log.info("POST request to create medicine with name: {}", medicine.getName());
        MedicineDTO createdMedicine = medicineService.createMedicine(medicine);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMedicine);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineDTO> updateMedicine(@PathVariable Long id, @RequestBody Medicine medicine) {
        log.info("PUT request to update medicine with ID: {}", id);
        MedicineDTO updatedMedicine = medicineService.updateMedicine(id, medicine);
        return ResponseEntity.ok(updatedMedicine);
    }

    @PatchMapping("/{id}/quantity")
    public ResponseEntity<MedicineDTO> updateMedicineQuantity(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        log.info("PATCH request to update quantity for medicine ID {}: new quantity = {}", id, quantity);
        MedicineDTO updatedMedicine = medicineService.updateMedicineQuantity(id, quantity);
        return ResponseEntity.ok(updatedMedicine);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable Long id) {
        log.info("DELETE request to delete medicine with ID: {}", id);
        medicineService.deleteMedicine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/mark-expired")
    public ResponseEntity<Void> markExpiredMedicines() {
        log.info("POST request to mark expired medicines");
        medicineService.markExpiredMedicines();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/exists/name")
    public ResponseEntity<Boolean> checkNameExists(@RequestParam String name) {
        log.info("GET request to check if medicine name exists: {}", name);
        boolean exists = medicineService.existsByName(name);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countMedicines() {
        log.info("GET request to count all medicines");
        long count = medicineService.countAllMedicines();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/category/{category}")
    public ResponseEntity<Long> countMedicinesByCategory(@PathVariable String category) {
        log.info("GET request to count medicines in category: {}", category);
        long count = medicineService.countMedicinesByCategory(category);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/company/{companyId}")
    public ResponseEntity<Long> countMedicinesByCompany(@PathVariable Long companyId) {
        log.info("GET request to count medicines from company: {}", companyId);
        long count = medicineService.countMedicinesByCompany(companyId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/expired")
    public ResponseEntity<Long> countExpiredMedicines() {
        log.info("GET request to count expired medicines");
        long count = medicineService.countExpiredMedicines();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/low-stock")
    public ResponseEntity<Long> countLowStockMedicines(@RequestParam(defaultValue = "10") Integer threshold) {
        log.info("GET request to count low stock medicines (threshold: {})", threshold);
        long count = medicineService.countLowStockMedicines(threshold);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/inventory/total-value")
    public ResponseEntity<Double> getTotalInventoryValue() {
        log.info("GET request to get total inventory value");
        Double value = medicineService.getTotalInventoryValue();
        return ResponseEntity.ok(value);
    }

    @GetMapping("/inventory/value/category/{category}")
    public ResponseEntity<Double> getInventoryValueByCategory(@PathVariable String category) {
        log.info("GET request to get inventory value for category: {}", category);
        Double value = medicineService.getInventoryValueByCategory(category);
        return ResponseEntity.ok(value);
    }

    @GetMapping("/inventory/value/company/{companyId}")
    public ResponseEntity<Double> getInventoryValueByCompany(@PathVariable Long companyId) {
        log.info("GET request to get inventory value for company: {}", companyId);
        Double value = medicineService.getInventoryValueByCompany(companyId);
        return ResponseEntity.ok(value);
    }

    @GetMapping("/{medicineId}/sales/count")
    public ResponseEntity<Long> countSalesByMedicine(@PathVariable Long medicineId) {
        log.info("GET request to count sales for medicine: {}", medicineId);
        long count = medicineService.countSalesByMedicine(medicineId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{medicineId}/sales/revenue")
    public ResponseEntity<Double> getTotalRevenueByMedicine(@PathVariable Long medicineId) {
        log.info("GET request to get total revenue for medicine: {}", medicineId);
        Double revenue = medicineService.getTotalRevenueByMedicine(medicineId);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getMedicineStats() {
        log.info("GET request to get medicine statistics");
        long totalMedicines = medicineService.countAllMedicines();
        long expiredMedicines = medicineService.countExpiredMedicines();
        long lowStockMedicines = medicineService.countLowStockMedicines(10);
        Double totalInventoryValue = medicineService.getTotalInventoryValue();
        return ResponseEntity.ok(Map.of(
            "totalMedicines", totalMedicines,
            "expiredMedicines", expiredMedicines,
            "lowStockMedicines", lowStockMedicines,
            "totalInventoryValue", totalInventoryValue != null ? totalInventoryValue : 0.0
        ));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        log.info("GET request to fetch medicine categories");
        return ResponseEntity.ok(Arrays.asList(
            "ANTIBIOTIC",
            "PAINKILLER",
            "ANTIVIRAL",
            "ANTIFUNGAL",
            "ANTIHISTAMINE",
            "CARDIOVASCULAR",
            "DIABETES",
            "VITAMINS",
            "SUPPLEMENTS",
            "ANTISEPTIC",
            "OTHER"
        ));
    }
}
