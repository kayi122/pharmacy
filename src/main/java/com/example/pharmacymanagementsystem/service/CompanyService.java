package com.example.pharmacymanagementsystem.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pharmacymanagementsystem.dto.CompanyDTO;
import com.example.pharmacymanagementsystem.exception.DuplicateResourceException;
import com.example.pharmacymanagementsystem.exception.ResourceNotFoundException;
import com.example.pharmacymanagementsystem.model.Company;
import com.example.pharmacymanagementsystem.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<CompanyDTO> getAllCompaniesAsList() {
        log.info("Fetching all companies as list");
        return companyRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getCompaniesPaginated(int page, int size, String search) {
        List<Company> allCompanies;
        if (search != null && !search.isEmpty()) {
            allCompanies = companyRepository.searchByNameOrCountry(search);
        } else {
            allCompanies = companyRepository.findAll();
        }
        
        int start = page * size;
        int end = Math.min(start + size, allCompanies.size());
        List<Company> pageCompanies = allCompanies.subList(start, end);
        
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("content", pageCompanies.stream().map(this::convertToDTO).collect(Collectors.toList()));
        response.put("currentPage", page);
        response.put("totalItems", allCompanies.size());
        response.put("totalPages", (int) Math.ceil((double) allCompanies.size() / size));
        
        return response;
    }

    public List<CompanyDTO> getAllCompaniesWithMedicines() {
        log.info("Fetching all companies with medicines");
        return companyRepository.findAllWithMedicines().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CompanyDTO getCompanyById(Long id) {
        log.info("Fetching company with ID: {}", id);
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
        return convertToDTO(company);
    }

    public CompanyDTO getCompanyByIdWithMedicines(Long id) {
        log.info("Fetching company with ID and medicines: {}", id);
        Company company = companyRepository.findByIdWithMedicines(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
        return convertToDTO(company);
    }

    public CompanyDTO getCompanyByEmail(String email) {
        log.info("Fetching company with email: {}", email);
        Company company = companyRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with email: " + email));
        return convertToDTO(company);
    }

    public CompanyDTO getCompanyByRegistrationNumber(String registrationNumber) {
        log.info("Fetching company with registration number: {}", registrationNumber);
        Company company = companyRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                "Company not found with registration number: " + registrationNumber));
        return convertToDTO(company);
    }

    public CompanyDTO getCompanyByName(String name) {
        log.info("Fetching company with name: {}", name);
        Company company = companyRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with name: " + name));
        return convertToDTO(company);
    }

    public List<CompanyDTO> searchByName(String name) {
        log.info("Searching companies by name: {}", name);
        return companyRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CompanyDTO> getCompaniesByCountry(String country) {
        log.info("Fetching companies in country: {}", country);
        return companyRepository.findByCountry(country).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CompanyDTO> searchByPhone(String phone) {
        log.info("Searching companies by phone: {}", phone);
        return companyRepository.findByPhoneContaining(phone).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CompanyDTO> searchByNameOrCountry(String keyword) {
        log.info("Searching companies by name or country: {}", keyword);
        return companyRepository.searchByNameOrCountry(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CompanyDTO> getCompaniesByMedicineCategory(String category) {
        log.info("Fetching companies that supply category: {}", category);
        return companyRepository.findByMedicineCategory(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CompanyDTO> getAllCompaniesOrderedByName() {
        log.info("Fetching all companies ordered by name");
        return companyRepository.findAllByOrderByNameAsc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CompanyDTO> getCompaniesByCountryOrderedByName(String country) {
        log.info("Fetching companies in {} ordered by name", country);
        return companyRepository.findByCountryOrderByNameAsc(country).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CompanyDTO> getCompaniesWithExpiredMedicines() {
        log.info("Fetching companies with expired medicines");
        return companyRepository.findCompaniesWithExpiredMedicines().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CompanyDTO> getCompaniesWithLowStockMedicines(Integer threshold) {
        log.info("Fetching companies with low stock medicines (threshold: {})", threshold);
        return companyRepository.findCompaniesWithLowStockMedicines(threshold).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CompanyDTO createCompany(Company company) {
        log.info("Creating new company with name: {}", company.getName());

        if (companyRepository.existsByEmail(company.getEmail())) {
            throw new DuplicateResourceException("Company with email " + company.getEmail() + " already exists");
        }

        if (companyRepository.existsByRegistrationNumber(company.getRegistrationNumber())) {
            throw new DuplicateResourceException(
                    "Company with registration number " + company.getRegistrationNumber() + " already exists");
        }

        Company savedCompany = companyRepository.save(company);
        log.info("Company created successfully with ID: {}", savedCompany.getId());
        return convertToDTO(savedCompany);
    }

    @Transactional
    public CompanyDTO updateCompany(Long id, Company updatedCompany) {
        log.info("Updating company with ID: {}", id);

        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));

        if (!existingCompany.getEmail().equals(updatedCompany.getEmail())
                && companyRepository.existsByEmail(updatedCompany.getEmail())) {
            throw new DuplicateResourceException("Company with email " + updatedCompany.getEmail() + " already exists");
        }

        if (!existingCompany.getRegistrationNumber().equals(updatedCompany.getRegistrationNumber())
                && companyRepository.existsByRegistrationNumber(updatedCompany.getRegistrationNumber())) {
            throw new DuplicateResourceException(
                    "Company with registration number " + updatedCompany.getRegistrationNumber() + " already exists");
        }

        existingCompany.setName(updatedCompany.getName());
        existingCompany.setRegistrationNumber(updatedCompany.getRegistrationNumber());
        existingCompany.setEmail(updatedCompany.getEmail());
        existingCompany.setPhone(updatedCompany.getPhone());
        existingCompany.setCountry(updatedCompany.getCountry());
        existingCompany.setLocation(updatedCompany.getLocation());

        Company savedCompany = companyRepository.save(existingCompany);
        log.info("Company updated successfully with ID: {}", savedCompany.getId());
        return convertToDTO(savedCompany);
    }

    @Transactional
    public void deleteCompany(Long id) {
        log.info("Deleting company with ID: {}", id);

        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Company not found with id: " + id);
        }

        companyRepository.deleteById(id);
        log.info("Company deleted successfully with ID: {}", id);
    }

    public boolean existsByEmail(String email) {
        return companyRepository.existsByEmail(email);
    }

    public boolean existsByRegistrationNumber(String registrationNumber) {
        return companyRepository.existsByRegistrationNumber(registrationNumber);
    }

    public boolean hasMedicines(Long companyId) {
        return companyRepository.hasMedicines(companyId);
    }

    public long countAllCompanies() {
        return companyRepository.count();
    }

    public long countCompaniesByCountry(String country) {
        return companyRepository.countByCountry(country);
    }

    public long countMedicinesByCompany(Long companyId) {
        return companyRepository.countMedicinesByCompany(companyId);
    }

    public Double getTotalMedicineValueByCompany(Long companyId) {
        return companyRepository.getTotalMedicineValueByCompany(companyId);
    }

    public List<CompanyDTO> searchCompanies(String query) {
        log.info("Searching companies with query: {}", query);
        return companyRepository.searchByNameOrCountry(query).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CompanyDTO convertToDTO(Company company) {
        return new CompanyDTO(company);
    }
}
