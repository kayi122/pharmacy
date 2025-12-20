package com.example.pharmacymanagementsystem.controller;

import java.util.List;
import java.util.Map;

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

import com.example.pharmacymanagementsystem.dto.AgentDTO;
import com.example.pharmacymanagementsystem.model.Agent;
import com.example.pharmacymanagementsystem.service.AgentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
@Slf4j
public class AgentController {

    private final AgentService agentService;

    @GetMapping
    public ResponseEntity<List<AgentDTO>> getAllAgents() {
        log.info("GET request to fetch all agents");
        List<AgentDTO> agents = agentService.getAllAgents();
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Map<String, Object>> getAgentsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search) {
        log.info("GET request to fetch agents page {} with size {}", page, size);
        return ResponseEntity.ok(agentService.getAgentsPaginated(page, size, search));
    }

    @GetMapping("/with-relations")
    public ResponseEntity<List<AgentDTO>> getAllAgentsWithRelations() {
        log.info("GET request to fetch all agents with relations");
        List<AgentDTO> agents = agentService.getAllAgentsWithRelations();
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgentDTO> getAgentById(@PathVariable Long id) {
        log.info("GET request to fetch agent with ID: {}", id);
        AgentDTO agent = agentService.getAgentById(id);
        return ResponseEntity.ok(agent);
    }

    @GetMapping("/{id}/with-relations")
    public ResponseEntity<AgentDTO> getAgentByIdWithRelations(@PathVariable Long id) {
        log.info("GET request to fetch agent with ID and relationships: {}", id);
        AgentDTO agent = agentService.getAgentByIdWithRelations(id);
        return ResponseEntity.ok(agent);
    }

    @GetMapping("/email")
    public ResponseEntity<AgentDTO> getAgentByEmail(@RequestParam String email) {
        log.info("GET request to fetch agent with email: {}", email);
        AgentDTO agent = agentService.getAgentByEmail(email);
        return ResponseEntity.ok(agent);
    }

    @GetMapping("/phone")
    public ResponseEntity<AgentDTO> getAgentByPhone(@RequestParam String phone) {
        log.info("GET request to fetch agent with phone: {}", phone);
        AgentDTO agent = agentService.getAgentByPhone(phone);
        return ResponseEntity.ok(agent);
    }

    @GetMapping("/search/firstname")
    public ResponseEntity<List<AgentDTO>> searchByFirstName(@RequestParam String name) {
        log.info("GET request to search agents by first name: {}", name);
        List<AgentDTO> agents = agentService.searchByFirstName(name);
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/search/lastname")
    public ResponseEntity<List<AgentDTO>> searchByLastName(@RequestParam String name) {
        log.info("GET request to search agents by last name: {}", name);
        List<AgentDTO> agents = agentService.searchByLastName(name);
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AgentDTO>> searchByName(@RequestParam String name) {
        log.info("GET request to search agents by name: {}", name);
        List<AgentDTO> agents = agentService.searchByName(name);
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/search/company")
    public ResponseEntity<List<AgentDTO>> searchByCompanyName(@RequestParam String companyName) {
        log.info("GET request to search agents by company name: {}", companyName);
        List<AgentDTO> agents = agentService.searchByCompanyName(companyName);
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<AgentDTO>> getAgentsByLocation(@PathVariable Long locationId) {
        log.info("GET request to fetch agents in location: {}", locationId);
        List<AgentDTO> agents = agentService.getAgentsByLocation(locationId);
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/location/{locationId}/with-details")
    public ResponseEntity<List<AgentDTO>> getAgentsByLocationWithDetails(@PathVariable Long locationId) {
        log.info("GET request to fetch agents in location with details: {}", locationId);
        List<AgentDTO> agents = agentService.getAgentsByLocationWithDetails(locationId);
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/with-medicines")
    public ResponseEntity<List<AgentDTO>> getAgentsWithMedicines() {
        log.info("GET request to fetch agents with medicines");
        List<AgentDTO> agents = agentService.getAgentsWithMedicines();
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/medicine/{medicineId}")
    public ResponseEntity<List<AgentDTO>> getAgentsByMedicine(@PathVariable Long medicineId) {
        log.info("GET request to fetch agents for medicine: {}", medicineId);
        List<AgentDTO> agents = agentService.getAgentsByMedicine(medicineId);
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/ordered-by-name")
    public ResponseEntity<List<AgentDTO>> getAllAgentsOrderedByName() {
        log.info("GET request to fetch agents ordered by name");
        List<AgentDTO> agents = agentService.getAllAgentsOrderedByName();
        return ResponseEntity.ok(agents);
    }

    @PostMapping
    public ResponseEntity<AgentDTO> createAgent(@RequestBody Agent agent) {
        log.info("POST request to create agent with email: {}", agent.getEmail());
        AgentDTO createdAgent = agentService.createAgent(agent);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAgent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgentDTO> updateAgent(@PathVariable Long id, @RequestBody Agent agent) {
        log.info("PUT request to update agent with ID: {}", id);
        AgentDTO updatedAgent = agentService.updateAgent(id, agent);
        return ResponseEntity.ok(updatedAgent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgent(@PathVariable Long id) {
        log.info("DELETE request to delete agent with ID: {}", id);
        agentService.deleteAgent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{agentId}/medicine/{medicineId}")
    public ResponseEntity<AgentDTO> assignMedicineToAgent(
            @PathVariable Long agentId,
            @PathVariable Long medicineId) {
        log.info("POST request to assign medicine {} to agent {}", medicineId, agentId);
        AgentDTO agent = agentService.assignMedicineToAgent(agentId, medicineId);
        return ResponseEntity.ok(agent);
    }

    @DeleteMapping("/{agentId}/medicine/{medicineId}")
    public ResponseEntity<AgentDTO> removeMedicineFromAgent(
            @PathVariable Long agentId,
            @PathVariable Long medicineId) {
        log.info("DELETE request to remove medicine {} from agent {}", medicineId, agentId);
        AgentDTO agent = agentService.removeMedicineFromAgent(agentId, medicineId);
        return ResponseEntity.ok(agent);
    }

    @GetMapping("/exists/email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        log.info("GET request to check if email exists: {}", email);
        boolean exists = agentService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/phone")
    public ResponseEntity<Boolean> checkPhoneExists(@RequestParam String phone) {
        log.info("GET request to check if phone exists: {}", phone);
        boolean exists = agentService.existsByPhone(phone);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/{agentId}/has-medicines")
    public ResponseEntity<Boolean> hasMedicines(@PathVariable Long agentId) {
        log.info("GET request to check if agent {} has medicines", agentId);
        boolean has = agentService.hasMedicines(agentId);
        return ResponseEntity.ok(has);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countAgents() {
        log.info("GET request to count all agents");
        long count = agentService.countAllAgents();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/location/{locationId}")
    public ResponseEntity<Long> countAgentsByLocation(@PathVariable Long locationId) {
        log.info("GET request to count agents in location: {}", locationId);
        long count = agentService.countAgentsByLocation(locationId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/company")
    public ResponseEntity<Long> countAgentsByCompanyName(@RequestParam String companyName) {
        log.info("GET request to count agents by company: {}", companyName);
        long count = agentService.countAgentsByCompanyName(companyName);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{agentId}/medicines/count")
    public ResponseEntity<Long> countMedicinesByAgent(@PathVariable Long agentId) {
        log.info("GET request to count medicines for agent: {}", agentId);
        long count = agentService.countMedicinesByAgent(agentId);
        return ResponseEntity.ok(count);
    }
}
