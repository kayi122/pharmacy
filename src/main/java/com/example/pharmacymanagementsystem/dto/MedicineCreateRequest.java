package com.example.pharmacymanagementsystem.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineCreateRequest {

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
    private Long companyId;  // Just accept the ID directly
}
