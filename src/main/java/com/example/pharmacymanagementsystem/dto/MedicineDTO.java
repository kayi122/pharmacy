package com.example.pharmacymanagementsystem.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.example.pharmacymanagementsystem.model.Medicine;

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
public class MedicineDTO {

    private Long id;
    private String name;
    private String category;
    private String description;
    private Double purchasePrice;
    private Double sellingPrice;
    private Integer quantity;
    private LocalDate manufactureDate;
    private LocalDate expiryDate;
    private LocalDate entryDate;
    private String batchNumber;
    private Boolean isExpired;

    // Company information
    private Long companyId;
    private String companyName;
    private String companyCountry;

    // Agent information
    private Set<Long> agentIds = new HashSet<>();
    private Set<String> agentNames = new HashSet<>();
    private Long agentCount;

    // Sales information
    private Long totalSalesCount;
    private Double totalRevenue;

    // Calculated fields
    private Double profitMargin;
    private Double inventoryValue;
    private Long daysUntilExpiry;

    public MedicineDTO(Medicine medicine) {
        this.id = medicine.getId();
        this.name = medicine.getName();
        this.category = medicine.getCategory();
        this.description = medicine.getDescription();
        this.purchasePrice = medicine.getPurchasePrice();
        this.sellingPrice = medicine.getSellingPrice();
        this.quantity = medicine.getQuantity();
        this.manufactureDate = medicine.getManufactureDate();
        this.expiryDate = medicine.getExpiryDate();
        this.entryDate = medicine.getEntryDate();
        this.batchNumber = medicine.getBatchNumber();
        this.isExpired = medicine.getIsExpired();

        // Company info
        if (medicine.getCompany() != null) {
            this.companyId = medicine.getCompany().getId();
            this.companyName = medicine.getCompany().getName();
            this.companyCountry = medicine.getCompany().getCountry();
        }

        // Agent info
        if (medicine.getAgents() != null) {
            medicine.getAgents().forEach(agent -> {
                this.agentIds.add(agent.getId());
                this.agentNames.add(agent.getFirstName() + " " + agent.getLastName());
            });
            this.agentCount = (long) medicine.getAgents().size();
        } else {
            this.agentCount = 0L;
        }

        // Sales info
        if (medicine.getSales() != null) {
            this.totalSalesCount = (long) medicine.getSales().size();
            this.totalRevenue = medicine.getSales().stream()
                    .mapToDouble(sale -> sale.getTotalPrice())
                    .sum();
        } else {
            this.totalSalesCount = 0L;
            this.totalRevenue = 0.0;
        }

        // Calculated fields
        this.profitMargin = this.sellingPrice - this.purchasePrice;
        this.inventoryValue = this.purchasePrice * this.quantity;
        this.daysUntilExpiry = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), this.expiryDate);
    }
}
