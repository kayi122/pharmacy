package com.example.pharmacymanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pharmacymanagementsystem.model.Agent;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {

    // Check if agent exists by email
    Boolean existsByEmail(String email);

    // Check if agent exists by phone
    Boolean existsByPhone(String phone);

    // Find agent by email
    Optional<Agent> findByEmail(String email);

    // Find agent by phone
    Optional<Agent> findByPhone(String phone);

    // Find agents by first name (case insensitive)
    List<Agent> findByFirstNameContainingIgnoreCase(String firstName);

    // Find agents by last name (case insensitive)
    List<Agent> findByLastNameContainingIgnoreCase(String lastName);

    // Find agents by company name
    List<Agent> findByCompanyNameContainingIgnoreCase(String companyName);

    // Search agents by name (first or last)
    @Query("SELECT a FROM Agent a WHERE LOWER(a.firstName) LIKE LOWER(CONCAT('%', :name, '%')) "
            + "OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Agent> searchByName(@Param("name") String name);

    // Find agents by location
    List<Agent> findByLocationId(Long locationId);

    // Find agents with medicines
    @Query("SELECT DISTINCT a FROM Agent a LEFT JOIN FETCH a.medicines WHERE SIZE(a.medicines) > 0")
    List<Agent> findAllAgentsWithMedicines();

    // Find agents for a specific medicine
    @Query("SELECT a FROM Agent a JOIN a.medicines m WHERE m.id = :medicineId")
    List<Agent> findAgentsByMedicineId(@Param("medicineId") Long medicineId);

    // Count agents by location
    Long countByLocationId(Long locationId);

    // Count agents by company name
    Long countByCompanyName(String companyName);

    // Find all agents with their relationships
    @Query("SELECT DISTINCT a FROM Agent a "
            + "LEFT JOIN FETCH a.medicines "
            + "LEFT JOIN FETCH a.location")
    List<Agent> findAllWithRelations();

    // Find agent by ID with relationships
    @Query("SELECT a FROM Agent a "
            + "LEFT JOIN FETCH a.medicines "
            + "LEFT JOIN FETCH a.location "
            + "WHERE a.id = :agentId")
    Optional<Agent> findByIdWithRelations(@Param("agentId") Long agentId);

    // Find agents ordered by name
    List<Agent> findAllByOrderByLastNameAscFirstNameAsc();

    // Find agents by location with details
    @Query("SELECT a FROM Agent a "
            + "LEFT JOIN FETCH a.medicines "
            + "LEFT JOIN FETCH a.location "
            + "WHERE a.location.id = :locationId")
    List<Agent> findByLocationIdWithDetails(@Param("locationId") Long locationId);

    // Check if agent has medicines
    @Query("SELECT COUNT(m) > 0 FROM Agent a JOIN a.medicines m WHERE a.id = :agentId")
    Boolean hasMedicines(@Param("agentId") Long agentId);

    // Count medicines for an agent
    @Query("SELECT COUNT(m) FROM Agent a JOIN a.medicines m WHERE a.id = :agentId")
    Long countMedicinesByAgent(@Param("agentId") Long agentId);
}
