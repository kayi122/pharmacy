package com.example.pharmacymanagementsystem.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pharmacymanagementsystem.dto.AgentDTO;
import com.example.pharmacymanagementsystem.exception.ResourceNotFoundException;
import com.example.pharmacymanagementsystem.model.Agent;
import com.example.pharmacymanagementsystem.model.Location;
import com.example.pharmacymanagementsystem.model.Medicine;
import com.example.pharmacymanagementsystem.repository.AgentRepository;
import com.example.pharmacymanagementsystem.repository.LocationRepository;
import com.example.pharmacymanagementsystem.repository.MedicineRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AgentService {

    private final AgentRepository agentRepository;
    private final LocationRepository locationRepository;
    private final MedicineRepository medicineRepository;

    public List<AgentDTO> getAllAgents() {
        log.info("Fetching all agents");
        return agentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AgentDTO> getAllAgentsWithRelations() {
        log.info("Fetching all agents with relations");
        return agentRepository.findAllWithRelations().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AgentDTO getAgentById(Long id) {
        log.info("Fetching agent with ID: {}", id);
        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found with id: " + id));
        return convertToDTO(agent);
    }

    public AgentDTO getAgentByIdWithRelations(Long id) {
        log.info("Fetching agent with ID and relationships: {}", id);
        Agent agent = agentRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found with id: " + id));
        return convertToDTO(agent);
    }

    public AgentDTO getAgentByEmail(String email) {
        log.info("Fetching agent with email: {}", email);
        Agent agent = agentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found with email: " + email));
        return convertToDTO(agent);
    }

    public AgentDTO getAgentByPhone(String phone) {
        log.info("Fetching agent with phone: {}", phone);
        Agent agent = agentRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found with phone: " + phone));
        return convertToDTO(agent);
    }

    public List<AgentDTO> searchByFirstName(String firstName) {
        log.info("Searching agents with first name: {}", firstName);
        return agentRepository.findByFirstNameContainingIgnoreCase(firstName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AgentDTO> searchByLastName(String lastName) {
        log.info("Searching agents with last name: {}", lastName);
        return agentRepository.findByLastNameContainingIgnoreCase(lastName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AgentDTO> searchByName(String name) {
        log.info("Searching agents by name: {}", name);
        return agentRepository.searchByName(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AgentDTO> searchByCompanyName(String companyName) {
        log.info("Searching agents by company name: {}", companyName);
        return agentRepository.findByCompanyNameContainingIgnoreCase(companyName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AgentDTO> getAgentsByLocation(Long locationId) {
        log.info("Fetching agents in location: {}", locationId);
        return agentRepository.findByLocationId(locationId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AgentDTO> getAgentsByLocationWithDetails(Long locationId) {
        log.info("Fetching agents in location with details: {}", locationId);
        return agentRepository.findByLocationIdWithDetails(locationId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AgentDTO> getAgentsWithMedicines() {
        log.info("Fetching all agents with medicines");
        return agentRepository.findAllAgentsWithMedicines().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AgentDTO> getAgentsByMedicine(Long medicineId) {
        log.info("Fetching agents for medicine: {}", medicineId);
        return agentRepository.findAgentsByMedicineId(medicineId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AgentDTO> getAllAgentsOrderedByName() {
        log.info("Fetching all agents ordered by name");
        return agentRepository.findAllByOrderByLastNameAscFirstNameAsc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AgentDTO createAgent(Agent agent) {
        log.info("Creating new agent with email: {}", agent.getEmail());

        if (agent.getLocation() != null && agent.getLocation().getId() != null) {
            Location location = locationRepository.findById(agent.getLocation().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Location not found"));
            agent.setLocation(location);
        }

        if (agent.getMedicines() != null && !agent.getMedicines().isEmpty()) {
            Set<Medicine> validatedMedicines = new HashSet<>();
            for (Medicine medicine : agent.getMedicines()) {
                Medicine existingMedicine = medicineRepository.findById(medicine.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + medicine.getId()));
                validatedMedicines.add(existingMedicine);
            }
            agent.setMedicines(validatedMedicines.stream().collect(Collectors.toList()));
        }

        Agent savedAgent = agentRepository.save(agent);
        log.info("Agent created successfully with ID: {}", savedAgent.getId());
        return convertToDTO(savedAgent);
    }

    @Transactional
    public AgentDTO updateAgent(Long id, Agent updatedAgent) {
        log.info("Updating agent with ID: {}", id);

        Agent existingAgent = agentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found with id: " + id));

        existingAgent.setFirstName(updatedAgent.getFirstName());
        existingAgent.setLastName(updatedAgent.getLastName());
        existingAgent.setEmail(updatedAgent.getEmail());
        existingAgent.setPhone(updatedAgent.getPhone());
        existingAgent.setCompanyName(updatedAgent.getCompanyName());

        if (updatedAgent.getLocation() != null && updatedAgent.getLocation().getId() != null) {
            Location location = locationRepository.findById(updatedAgent.getLocation().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Location not found"));
            existingAgent.setLocation(location);
        }

        Agent savedAgent = agentRepository.save(existingAgent);
        log.info("Agent updated successfully with ID: {}", savedAgent.getId());
        return convertToDTO(savedAgent);
    }

    @Transactional
    public void deleteAgent(Long id) {
        log.info("Deleting agent with ID: {}", id);

        if (!agentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agent not found with id: " + id);
        }

        agentRepository.deleteById(id);
        log.info("Agent deleted successfully with ID: {}", id);
    }

    @Transactional
    public AgentDTO assignMedicineToAgent(Long agentId, Long medicineId) {
        log.info("Assigning medicine {} to agent {}", medicineId, agentId);

        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found with id: " + agentId));

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + medicineId));

        if (agent.getMedicines() == null) {
            agent.setMedicines(new java.util.ArrayList<>());
        }

        if (!agent.getMedicines().contains(medicine)) {
            agent.getMedicines().add(medicine);
        }

        Agent savedAgent = agentRepository.save(agent);
        log.info("Medicine assigned successfully");
        return convertToDTO(savedAgent);
    }

    @Transactional
    public AgentDTO removeMedicineFromAgent(Long agentId, Long medicineId) {
        log.info("Removing medicine {} from agent {}", medicineId, agentId);

        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found with id: " + agentId));

        if (agent.getMedicines() != null) {
            agent.getMedicines().removeIf(medicine -> medicine.getId().equals(medicineId));
        }

        Agent savedAgent = agentRepository.save(agent);
        log.info("Medicine removed successfully");
        return convertToDTO(savedAgent);
    }

    public boolean existsByEmail(String email) {
        return agentRepository.existsByEmail(email);
    }

    public boolean existsByPhone(String phone) {
        return agentRepository.existsByPhone(phone);
    }

    public boolean hasMedicines(Long agentId) {
        return agentRepository.hasMedicines(agentId);
    }

    public long countAllAgents() {
        return agentRepository.count();
    }

    public long countAgentsByLocation(Long locationId) {
        return agentRepository.countByLocationId(locationId);
    }

    public long countAgentsByCompanyName(String companyName) {
        return agentRepository.countByCompanyName(companyName);
    }

    public long countMedicinesByAgent(Long agentId) {
        return agentRepository.countMedicinesByAgent(agentId);
    }

    private AgentDTO convertToDTO(Agent agent) {
        return new AgentDTO(agent);
    }
}
