package com.example.pharmacymanagementsystem.dto;

import java.util.HashSet;
import java.util.Set;

import com.example.pharmacymanagementsystem.model.Agent;
import com.example.pharmacymanagementsystem.util.LocationHelper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgentDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String companyName;

    // Location information
    private Long locationId;
    private String locationProvince;
    private String locationDistrict;
    private String locationSector;

    // Medicine information
    private Set<Long> medicineIds = new HashSet<>();
    private Set<String> medicineNames = new HashSet<>();
    private Long medicineCount;

    public AgentDTO(Agent agent) {
        this.id = agent.getId();
        this.firstName = agent.getFirstName();
        this.lastName = agent.getLastName();
        this.email = agent.getEmail();
        this.phone = agent.getPhone();
        this.companyName = agent.getCompanyName();

        if (agent.getLocation() != null) {
            this.locationId = agent.getLocation().getId();
            this.locationProvince = LocationHelper.getProvince(agent.getLocation());
            this.locationDistrict = LocationHelper.getDistrict(agent.getLocation());
            this.locationSector = LocationHelper.getSector(agent.getLocation());
        }

        if (agent.getMedicines() != null) {
            agent.getMedicines().forEach(medicine -> {
                this.medicineIds.add(medicine.getId());
                this.medicineNames.add(medicine.getName());
            });
            this.medicineCount = (long) agent.getMedicines().size();
        } else {
            this.medicineCount = 0L;
        }
    }
}
