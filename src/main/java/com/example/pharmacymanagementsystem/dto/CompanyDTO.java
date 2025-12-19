package com.example.pharmacymanagementsystem.dto;

import java.util.HashSet;
import java.util.Set;

import com.example.pharmacymanagementsystem.model.Company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {

    private Long id;
    private String name;
    private String registrationNumber;
    private String email;
    private String phone;
    private String country;
    private Long locationId;

    // Medicine information
    private Set<Long> medicineIds = new HashSet<>();
    private Set<String> medicineNames = new HashSet<>();
    private Long medicineCount;
    private Double totalMedicineValue;

    public CompanyDTO(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.registrationNumber = company.getRegistrationNumber();
        this.email = company.getEmail();
        this.phone = company.getPhone();
        this.country = company.getCountry();
        this.locationId = company.getLocation() != null ? company.getLocation().getId() : null;

        if (company.getMedicines() != null) {
            company.getMedicines().forEach(medicine -> {
                this.medicineIds.add(medicine.getId());
                this.medicineNames.add(medicine.getName());
            });
            this.medicineCount = (long) company.getMedicines().size();
            this.totalMedicineValue = company.getMedicines().stream()
                    .mapToDouble(m -> m.getPurchasePrice() * m.getQuantity())
                    .sum();
        } else {
            this.medicineCount = 0L;
            this.totalMedicineValue = 0.0;
        }
    }
}
