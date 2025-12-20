package com.example.pharmacymanagementsystem.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.example.pharmacymanagementsystem.dto.CompanyDTO;
import com.example.pharmacymanagementsystem.model.Company;
import com.example.pharmacymanagementsystem.service.CompanyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Slf4j
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/paginated")
    public ResponseEntity<Map<String, Object>> getCompaniesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search) {
        log.info("GET request to fetch companies page {} with size {}", page, size);
        return ResponseEntity.ok(companyService.getCompaniesPaginated(page, size, search));
    }

    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        log.info("GET request to fetch all companies");
        List<CompanyDTO> companies = companyService.getAllCompaniesAsList();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/with-medicines")
    public ResponseEntity<List<CompanyDTO>> getAllCompaniesWithMedicines() {
        log.info("GET request to fetch all companies with medicines");
        List<CompanyDTO> companies = companyService.getAllCompaniesWithMedicines();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable Long id) {
        log.info("GET request to fetch company with ID: {}", id);
        CompanyDTO company = companyService.getCompanyById(id);
        return ResponseEntity.ok(company);
    }

    @GetMapping("/{id}/with-medicines")
    public ResponseEntity<CompanyDTO> getCompanyByIdWithMedicines(@PathVariable Long id) {
        log.info("GET request to fetch company with ID and medicines: {}", id);
        CompanyDTO company = companyService.getCompanyByIdWithMedicines(id);
        return ResponseEntity.ok(company);
    }

    @GetMapping("/email")
    public ResponseEntity<CompanyDTO> getCompanyByEmail(@RequestParam String email) {
        log.info("GET request to fetch company with email: {}", email);
        CompanyDTO company = companyService.getCompanyByEmail(email);
        return ResponseEntity.ok(company);
    }

    @GetMapping("/registration-number")
    public ResponseEntity<CompanyDTO> getCompanyByRegistrationNumber(@RequestParam String registrationNumber) {
        log.info("GET request to fetch company with registration number: {}", registrationNumber);
        CompanyDTO company = companyService.getCompanyByRegistrationNumber(registrationNumber);
        return ResponseEntity.ok(company);
    }

    @GetMapping("/name")
    public ResponseEntity<CompanyDTO> getCompanyByName(@RequestParam String name) {
        log.info("GET request to fetch company with name: {}", name);
        CompanyDTO company = companyService.getCompanyByName(name);
        return ResponseEntity.ok(company);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CompanyDTO>> searchByName(@RequestParam String name) {
        log.info("GET request to search companies by name: {}", name);
        List<CompanyDTO> companies = companyService.searchByName(name);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<CompanyDTO>> getCompaniesByCountry(@PathVariable String country) {
        log.info("GET request to fetch companies in country: {}", country);
        List<CompanyDTO> companies = companyService.getCompaniesByCountry(country);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/search/phone")
    public ResponseEntity<List<CompanyDTO>> searchByPhone(@RequestParam String phone) {
        log.info("GET request to search companies by phone: {}", phone);
        List<CompanyDTO> companies = companyService.searchByPhone(phone);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/search/advanced")
    public ResponseEntity<List<CompanyDTO>> searchByNameOrCountry(@RequestParam String keyword) {
        log.info("GET request to search companies by name or country: {}", keyword);
        List<CompanyDTO> companies = companyService.searchByNameOrCountry(keyword);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/medicine-category/{category}")
    public ResponseEntity<List<CompanyDTO>> getCompaniesByMedicineCategory(@PathVariable String category) {
        log.info("GET request to fetch companies that supply category: {}", category);
        List<CompanyDTO> companies = companyService.getCompaniesByMedicineCategory(category);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/ordered-by-name")
    public ResponseEntity<List<CompanyDTO>> getAllCompaniesOrderedByName() {
        log.info("GET request to fetch companies ordered by name");
        List<CompanyDTO> companies = companyService.getAllCompaniesOrderedByName();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/country/{country}/ordered-by-name")
    public ResponseEntity<List<CompanyDTO>> getCompaniesByCountryOrderedByName(@PathVariable String country) {
        log.info("GET request to fetch companies in {} ordered by name", country);
        List<CompanyDTO> companies = companyService.getCompaniesByCountryOrderedByName(country);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/with-expired-medicines")
    public ResponseEntity<List<CompanyDTO>> getCompaniesWithExpiredMedicines() {
        log.info("GET request to fetch companies with expired medicines");
        List<CompanyDTO> companies = companyService.getCompaniesWithExpiredMedicines();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/with-low-stock-medicines")
    public ResponseEntity<List<CompanyDTO>> getCompaniesWithLowStockMedicines(
            @RequestParam(defaultValue = "10") Integer threshold) {
        log.info("GET request to fetch companies with low stock medicines (threshold: {})", threshold);
        List<CompanyDTO> companies = companyService.getCompaniesWithLowStockMedicines(threshold);
        return ResponseEntity.ok(companies);
    }

    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody Company company) {
        log.info("POST request to create company with name: {}", company.getName());
        CompanyDTO createdCompany = companyService.createCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable Long id, @RequestBody Company company) {
        log.info("PUT request to update company with ID: {}", id);
        CompanyDTO updatedCompany = companyService.updateCompany(id, company);
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        log.info("DELETE request to delete company with ID: {}", id);
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        log.info("GET request to check if email exists: {}", email);
        boolean exists = companyService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/registration-number")
    public ResponseEntity<Boolean> checkRegistrationNumberExists(@RequestParam String registrationNumber) {
        log.info("GET request to check if registration number exists: {}", registrationNumber);
        boolean exists = companyService.existsByRegistrationNumber(registrationNumber);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/{companyId}/has-medicines")
    public ResponseEntity<Boolean> hasMedicines(@PathVariable Long companyId) {
        log.info("GET request to check if company {} has medicines", companyId);
        boolean has = companyService.hasMedicines(companyId);
        return ResponseEntity.ok(has);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countCompanies() {
        log.info("GET request to count all companies");
        long count = companyService.countAllCompanies();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/country/{country}")
    public ResponseEntity<Long> countCompaniesByCountry(@PathVariable String country) {
        log.info("GET request to count companies in country: {}", country);
        long count = companyService.countCompaniesByCountry(country);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{companyId}/medicines/count")
    public ResponseEntity<Long> countMedicinesByCompany(@PathVariable Long companyId) {
        log.info("GET request to count medicines for company: {}", companyId);
        long count = companyService.countMedicinesByCompany(companyId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{companyId}/medicines/total-value")
    public ResponseEntity<Double> getTotalMedicineValueByCompany(@PathVariable Long companyId) {
        log.info("GET request to get total medicine value for company: {}", companyId);
        Double value = companyService.getTotalMedicineValueByCompany(companyId);
        return ResponseEntity.ok(value);
    }
}
