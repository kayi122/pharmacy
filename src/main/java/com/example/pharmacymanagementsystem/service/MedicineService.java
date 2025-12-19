package com.example.pharmacymanagementsystem.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pharmacymanagementsystem.dto.MedicineDTO;
import com.example.pharmacymanagementsystem.exception.ResourceNotFoundException;
import com.example.pharmacymanagementsystem.model.Agent;
import com.example.pharmacymanagementsystem.model.Company;
import com.example.pharmacymanagementsystem.model.Medicine;
import com.example.pharmacymanagementsystem.repository.AgentRepository;
import com.example.pharmacymanagementsystem.repository.CompanyRepository;
import com.example.pharmacymanagementsystem.repository.MedicineRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final CompanyRepository companyRepository;
    private final AgentRepository agentRepository;

    public List<MedicineDTO> getAllMedicines() {
        log.info("Fetching all medicines");
        return medicineRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineDTO> getAllMedicinesWithRelations() {
        log.info("Fetching all medicines with relations");
        return medicineRepository.findAllWithRelations().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MedicineDTO getMedicineById(Long id) {
        log.info("Fetching medicine with ID: {}", id);
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));
        return convertToDTO(medicine);
    }

    public MedicineDTO getMedicineByIdWithRelations(Long id) {
        log.info("Fetching medicine with ID and relationships: {}", id);
        Medicine medicine = medicineRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));
        return convertToDTO(medicine);
    }

    public MedicineDTO getMedicineByName(String name) {
        log.info("Fetching medicine with name: {}", name);
        Medicine medicine = medicineRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with name: " + name));
        return convertToDTO(medicine);
    }

    public List<MedicineDTO> searchByName(String name) {
        log.info("Searching medicines by name: {}", name);
        return medicineRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineDTO> getMedicinesByCategory(String category) {
        log.info("Fetching medicines in category: {}", category);
        return medicineRepository.findByCategory(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineDTO> getMedicinesByBatchNumber(String batchNumber) {
        log.info("Fetching medicines with batch number: {}", batchNumber);
        return medicineRepository.findByBatchNumber(batchNumber).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineDTO> getMedicinesByCompany(Long companyId) {
        log.info("Fetching medicines from company: {}", companyId);
        return medicineRepository.findByCompanyId(companyId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineDTO> getMedicinesByCompanyWithDetails(Long companyId) {
        log.info("Fetching medicines from company with details: {}", companyId);
        return medicineRepository.findByCompanyIdWithDetails(companyId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineDTO> getExpiredMedicines() {
        log.info("Fetching expired medicines");
        return medicineRepository.findByIsExpiredTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineDTO> getNonExpiredMedicines() {
        log.info("Fetching non-expired medicines");
        return medicineRepository.findByIsExpiredFalse().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineDTO> getMedicinesExpiringBefore(LocalDate date) {
        log.info("Fetching medicines expiring before: {}", date);
        return medicineRepository.findByExpiryDateBefore(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineDTO> getMedicinesExpiringBetween(LocalDate startDate, LocalDate endDate) {
        log.info("Fetching medicines expiring between {} and {}", startDate, endDate);
        return medicineRepository.findByExpiryDateBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineDTO> getMedicinesExpiringSoon(Integer days) {
        log.info("Fetching medicines expiring within {} days", days);
        LocalDate futureDate = LocalDate.now().plusDays(days);
        return medicineRepository.findExpiringSoon(futureDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineDTO> getLowStockMedicines(Integer threshold) {
        log.info("Fetching low stock medicines (threshold: {})", threshold);
        return medicineRepository.findLowStockMedicines(threshold).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineDTO> getMedicinesByPriceRange(Double minPrice, Double maxPrice) {
        log.info("Fetching medicines in price range: {} - {}", minPrice, maxPrice);
        return medicineRepository.findByPriceRange(minPrice, maxPrice).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineDTO> searchByNameOrCategory(String keyword) {
        log.info("Searching medicines by name or category: {}", keyword);
        return medicineRepository.searchByNameOrCategory(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineDTO> getAllMedicinesOrderedByName() {
        log.info("Fetching all medicines ordered by name");
        return medicineRepository.findAllByOrderByNameAsc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineDTO> getAllMedicinesOrderedByExpiryDate() {
        log.info("Fetching all medicines ordered by expiry date");
        return medicineRepository.findAllByOrderByExpiryDateAsc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineDTO> getAllMedicinesOrderedByQuantity() {
        log.info("Fetching all medicines ordered by quantity");
        return medicineRepository.findAllByOrderByQuantityAsc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineDTO> getMedicinesByAgent(Long agentId) {
        log.info("Fetching medicines for agent: {}", agentId);
        return medicineRepository.findByAgentId(agentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MedicineDTO createMedicine(Medicine medicine) {
        log.info("Creating new medicine with name: {}", medicine.getName());

        if (medicine.getCompany() != null && medicine.getCompany().getId() != null) {
            Company company = companyRepository.findById(medicine.getCompany().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
            medicine.setCompany(company);
        }

        if (medicine.getAgents() != null && !medicine.getAgents().isEmpty()) {
            List<Agent> validatedAgents = new java.util.ArrayList<>();
            for (Agent agent : medicine.getAgents()) {
                Agent existingAgent = agentRepository.findById(agent.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Agent not found with id: " + agent.getId()));
                validatedAgents.add(existingAgent);
            }
            medicine.setAgents(validatedAgents);
        }

        // Check if medicine is expired
        if (medicine.getExpiryDate().isBefore(LocalDate.now())) {
            medicine.setIsExpired(true);
        }

        Medicine savedMedicine = medicineRepository.save(medicine);
        log.info("Medicine created successfully with ID: {}", savedMedicine.getId());
        return convertToDTO(savedMedicine);
    }

    @Transactional
    public MedicineDTO updateMedicine(Long id, Medicine updatedMedicine) {
        log.info("Updating medicine with ID: {}", id);

        Medicine existingMedicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));

        existingMedicine.setName(updatedMedicine.getName());
        existingMedicine.setCategory(updatedMedicine.getCategory());
        existingMedicine.setDescription(updatedMedicine.getDescription());
        existingMedicine.setPurchasePrice(updatedMedicine.getPurchasePrice());
        existingMedicine.setSellingPrice(updatedMedicine.getSellingPrice());
        existingMedicine.setQuantity(updatedMedicine.getQuantity());
        existingMedicine.setManufactureDate(updatedMedicine.getManufactureDate());
        existingMedicine.setExpiryDate(updatedMedicine.getExpiryDate());
        existingMedicine.setBatchNumber(updatedMedicine.getBatchNumber());

        // Update expiry status
        if (existingMedicine.getExpiryDate().isBefore(LocalDate.now())) {
            existingMedicine.setIsExpired(true);
        } else {
            existingMedicine.setIsExpired(false);
        }

        if (updatedMedicine.getCompany() != null && updatedMedicine.getCompany().getId() != null) {
            Company company = companyRepository.findById(updatedMedicine.getCompany().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
            existingMedicine.setCompany(company);
        }

        Medicine savedMedicine = medicineRepository.save(existingMedicine);
        log.info("Medicine updated successfully with ID: {}", savedMedicine.getId());
        return convertToDTO(savedMedicine);
    }

    @Transactional
    public void deleteMedicine(Long id) {
        log.info("Deleting medicine with ID: {}", id);

        if (!medicineRepository.existsById(id)) {
            throw new ResourceNotFoundException("Medicine not found with id: " + id);
        }

        medicineRepository.deleteById(id);
        log.info("Medicine deleted successfully with ID: {}", id);
    }

    @Transactional
    public MedicineDTO updateMedicineQuantity(Long id, Integer newQuantity) {
        log.info("Updating quantity for medicine ID {}: new quantity = {}", id, newQuantity);

        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));

        medicine.setQuantity(newQuantity);
        Medicine savedMedicine = medicineRepository.save(medicine);

        log.info("Medicine quantity updated successfully");
        return convertToDTO(savedMedicine);
    }

    @Transactional
    public void markExpiredMedicines() {
        log.info("Marking expired medicines");

        List<Medicine> medicines = medicineRepository.findByIsExpiredFalse();
        LocalDate today = LocalDate.now();

        for (Medicine medicine : medicines) {
            if (medicine.getExpiryDate().isBefore(today)) {
                medicine.setIsExpired(true);
            }
        }

        medicineRepository.saveAll(medicines);
        log.info("Expired medicines marked successfully");
    }

    public boolean existsByName(String name) {
        return medicineRepository.existsByName(name);
    }

    public long countAllMedicines() {
        return medicineRepository.count();
    }

    public long countMedicinesByCategory(String category) {
        return medicineRepository.countByCategory(category);
    }

    public long countMedicinesByCompany(Long companyId) {
        return medicineRepository.countByCompanyId(companyId);
    }

    public long countExpiredMedicines() {
        return medicineRepository.countByIsExpiredTrue();
    }

    public long countLowStockMedicines(Integer threshold) {
        return medicineRepository.countLowStockMedicines(threshold);
    }

    public Double getTotalInventoryValue() {
        return medicineRepository.getTotalInventoryValue();
    }

    public Double getInventoryValueByCategory(String category) {
        return medicineRepository.getInventoryValueByCategory(category);
    }

    public Double getInventoryValueByCompany(Long companyId) {
        return medicineRepository.getInventoryValueByCompany(companyId);
    }

    public long countSalesByMedicine(Long medicineId) {
        return medicineRepository.countSalesByMedicine(medicineId);
    }

    public Double getTotalRevenueByMedicine(Long medicineId) {
        return medicineRepository.getTotalRevenueByMedicine(medicineId);
    }

    public Map<String, Object> getMedicinesPaginated(int page, int size, String search) {
        List<Medicine> allMedicines;
        if (search != null && !search.isEmpty()) {
            allMedicines = medicineRepository.searchByNameOrCategory(search);
        } else {
            allMedicines = medicineRepository.findAll();
        }
        
        int start = page * size;
        int end = Math.min(start + size, allMedicines.size());
        List<Medicine> pageMedicines = allMedicines.subList(start, end);
        
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("content", pageMedicines.stream().map(this::convertToDTO).collect(Collectors.toList()));
        response.put("currentPage", page);
        response.put("totalItems", allMedicines.size());
        response.put("totalPages", (int) Math.ceil((double) allMedicines.size() / size));
        
        return response;
    }

    private MedicineDTO convertToDTO(Medicine medicine) {
        return new MedicineDTO(medicine);
    }
}
