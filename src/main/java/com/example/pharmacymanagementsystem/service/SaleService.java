package com.example.pharmacymanagementsystem.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pharmacymanagementsystem.dto.SaleDTO;
import com.example.pharmacymanagementsystem.exception.BadRequestException;
import com.example.pharmacymanagementsystem.exception.ExpiredMedicineException;
import com.example.pharmacymanagementsystem.exception.InsufficientStockException;
import com.example.pharmacymanagementsystem.exception.ResourceNotFoundException;
import com.example.pharmacymanagementsystem.model.Medicine;
import com.example.pharmacymanagementsystem.model.Sale;
import com.example.pharmacymanagementsystem.model.User;
import com.example.pharmacymanagementsystem.repository.MedicineRepository;
import com.example.pharmacymanagementsystem.repository.SaleRepository;
import com.example.pharmacymanagementsystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SaleService {

    private final SaleRepository saleRepository;
    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;

    public List<SaleDTO> getAllSales() {
        log.info("Fetching all sales");
        return saleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SaleDTO> getAllSalesWithDetails() {
        log.info("Fetching all sales with details");
        return saleRepository.findAllWithDetails().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public SaleDTO getSaleById(Long id) {
        log.info("Fetching sale with ID: {}", id);
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));
        return convertToDTO(sale);
    }

    public List<SaleDTO> getSalesByMedicine(Long medicineId) {
        log.info("Fetching sales for medicine: {}", medicineId);
        return saleRepository.findByMedicineId(medicineId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SaleDTO> getSalesByMedicineWithDetails(Long medicineId) {
        log.info("Fetching sales for medicine with details: {}", medicineId);
        return saleRepository.findByMedicineIdWithDetails(medicineId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SaleDTO> getSalesByUser(Long userId) {
        log.info("Fetching sales for user: {}", userId);
        return saleRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SaleDTO> getSalesByUserWithDetails(Long userId) {
        log.info("Fetching sales for user with details: {}", userId);
        return saleRepository.findByUserIdWithDetails(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SaleDTO> searchByCustomerName(String customerName) {
        log.info("Searching sales by customer name: {}", customerName);
        return saleRepository.findByCustomerNameContainingIgnoreCase(customerName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SaleDTO> getSalesByCustomerPhone(String customerPhone) {
        log.info("Fetching sales by customer phone: {}", customerPhone);
        return saleRepository.findByCustomerPhone(customerPhone).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SaleDTO> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching sales between {} and {}", startDate, endDate);
        return saleRepository.findBySaleDateBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SaleDTO> getSalesAfterDate(LocalDateTime date) {
        log.info("Fetching sales after: {}", date);
        return saleRepository.findBySaleDateAfter(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SaleDTO> getSalesBeforeDate(LocalDateTime date) {
        log.info("Fetching sales before: {}", date);
        return saleRepository.findBySaleDateBefore(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SaleDTO> getAllSalesOrderedByDate() {
        log.info("Fetching all sales ordered by date");
        return saleRepository.findAllByOrderBySaleDateDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SaleDTO> getAllSalesOrderedByTotalPrice() {
        log.info("Fetching all sales ordered by total price");
        return saleRepository.findAllByOrderByTotalPriceDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SaleDTO> getTodaySales() {
        log.info("Fetching today's sales");
        return saleRepository.findTodaySales().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SaleDTO> getWeeklySales() {
        log.info("Fetching this week's sales");
        LocalDateTime weekStart = LocalDateTime.now().minusDays(7);
        return saleRepository.findWeeklySales(weekStart).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SaleDTO> getMonthlySales(int month, int year) {
        log.info("Fetching sales for month {} of year {}", month, year);
        return saleRepository.findMonthlySales(month, year).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SaleDTO> getHighValueSales(Double threshold) {
        log.info("Fetching high value sales (threshold: {})", threshold);
        return saleRepository.findHighValueSales(threshold).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SaleDTO createSale(Sale sale) {
        log.info("Creating new sale for medicine: {}", sale.getMedicine() != null ? sale.getMedicine().getId() : "null");

        // Validate inputs
        if (sale.getMedicine() == null || sale.getMedicine().getId() == null) {
            throw new BadRequestException("Medicine information is required");
        }

        if (sale.getUser() == null || sale.getUser().getId() == null) {
            throw new BadRequestException("User information is required");
        }

        // Validate medicine
        Medicine medicine = medicineRepository.findById(sale.getMedicine().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + sale.getMedicine().getId()));

        // Check if medicine is expired
        if (medicine.getIsExpired()) {
            throw new ExpiredMedicineException("Cannot sell expired medicine: " + medicine.getName());
        }

        // Check stock availability
        if (medicine.getQuantity() < sale.getQuantity()) {
            throw new InsufficientStockException(
                    "Insufficient stock for medicine: " + medicine.getName()
                    + ". Available: " + medicine.getQuantity() + ", Requested: " + sale.getQuantity());
        }

        // Validate quantity
        if (sale.getQuantity() <= 0) {
            throw new BadRequestException("Sale quantity must be greater than zero");
        }

        // Validate user
        User user = userRepository.findById(sale.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + sale.getUser().getId()));

        // Set sale date if not provided
        if (sale.getSaleDate() == null) {
            sale.setSaleDate(LocalDateTime.now());
        }

        // Calculate total price if not provided
        if (sale.getTotalPrice() == null || sale.getTotalPrice() == 0) {
            sale.setTotalPrice(medicine.getSellingPrice() * sale.getQuantity());
        }

        sale.setMedicine(medicine);
        sale.setUser(user);

        // Update medicine quantity
        medicine.setQuantity(medicine.getQuantity() - sale.getQuantity());
        medicineRepository.save(medicine);

        Sale savedSale = saleRepository.save(sale);
        log.info("Sale created successfully with ID: {}", savedSale.getId());
        return convertToDTO(savedSale);
    }

    @Transactional
    public SaleDTO updateSale(Long id, Sale updatedSale) {
        log.info("Updating sale with ID: {}", id);

        Sale existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));

        // Restore medicine quantity from old sale
        Medicine oldMedicine = existingSale.getMedicine();
        oldMedicine.setQuantity(oldMedicine.getQuantity() + existingSale.getQuantity());

        // Validate new medicine
        Medicine newMedicine = medicineRepository.findById(updatedSale.getMedicine().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));

        // Check if new medicine is expired
        if (newMedicine.getIsExpired()) {
            // Restore old medicine quantity
            oldMedicine.setQuantity(oldMedicine.getQuantity() - existingSale.getQuantity());
            medicineRepository.save(oldMedicine);
            throw new ExpiredMedicineException("Cannot sell expired medicine: " + newMedicine.getName());
        }

        // Check new stock availability
        if (newMedicine.getQuantity() < updatedSale.getQuantity()) {
            // Restore old medicine quantity
            oldMedicine.setQuantity(oldMedicine.getQuantity() - existingSale.getQuantity());
            medicineRepository.save(oldMedicine);
            throw new InsufficientStockException(
                    "Insufficient stock for medicine: " + newMedicine.getName()
                    + ". Available: " + newMedicine.getQuantity() + ", Requested: " + updatedSale.getQuantity());
        }

        // Update new medicine quantity
        newMedicine.setQuantity(newMedicine.getQuantity() - updatedSale.getQuantity());

        // Save medicine quantities
        if (!oldMedicine.getId().equals(newMedicine.getId())) {
            medicineRepository.save(oldMedicine);
        }
        medicineRepository.save(newMedicine);

        existingSale.setMedicine(newMedicine);
        existingSale.setQuantity(updatedSale.getQuantity());
        existingSale.setTotalPrice(updatedSale.getTotalPrice());
        existingSale.setCustomerName(updatedSale.getCustomerName());
        existingSale.setCustomerPhone(updatedSale.getCustomerPhone());

        Sale savedSale = saleRepository.save(existingSale);
        log.info("Sale updated successfully with ID: {}", savedSale.getId());
        return convertToDTO(savedSale);
    }

    @Transactional
    public void deleteSale(Long id) {
        log.info("Deleting sale with ID: {}", id);

        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));

        // Restore medicine quantity
        Medicine medicine = sale.getMedicine();
        medicine.setQuantity(medicine.getQuantity() + sale.getQuantity());
        medicineRepository.save(medicine);

        saleRepository.deleteById(id);
        log.info("Sale deleted successfully with ID: {}", id);
    }

    public long countAllSales() {
        return saleRepository.count();
    }

    public long countSalesByMedicine(Long medicineId) {
        return saleRepository.countByMedicineId(medicineId);
    }

    public long countSalesByUser(Long userId) {
        return saleRepository.countByUserId(userId);
    }

    public long countSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.countSalesByDateRange(startDate, endDate);
    }

    public Double getTotalRevenue() {
        return saleRepository.getTotalRevenue();
    }

    public Double getTotalRevenueByMedicine(Long medicineId) {
        return saleRepository.getTotalRevenueByMedicine(medicineId);
    }

    public Double getTotalRevenueByUser(Long userId) {
        return saleRepository.getTotalRevenueByUser(userId);
    }

    public Double getTotalRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.getTotalRevenueByDateRange(startDate, endDate);
    }

    public Double getTodayRevenue() {
        return saleRepository.getTodayRevenue();
    }

    public Long getTotalQuantitySold() {
        return saleRepository.getTotalQuantitySold();
    }

    public Long getTotalQuantitySoldByMedicine(Long medicineId) {
        return saleRepository.getTotalQuantitySoldByMedicine(medicineId);
    }

    public Double getAverageSaleValue() {
        return saleRepository.getAverageSaleValue();
    }

    public Double getAverageSaleValueByMedicine(Long medicineId) {
        return saleRepository.getAverageSaleValueByMedicine(medicineId);
    }

    public List<Object[]> getTopSellingMedicines() {
        return saleRepository.findTopSellingMedicines();
    }

    public List<Object[]> getSalesStatisticsByCategory() {
        return saleRepository.getSalesStatisticsByCategory();
    }

    public List<SaleDTO> searchSales(String query) {
        log.info("Searching sales with query: {}", query);
        return saleRepository.findByCustomerNameContainingIgnoreCase(query).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private SaleDTO convertToDTO(Sale sale) {
        return new SaleDTO(sale);
    }
}
