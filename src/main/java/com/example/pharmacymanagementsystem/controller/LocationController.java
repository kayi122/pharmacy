package com.example.pharmacymanagementsystem.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pharmacymanagementsystem.dto.LocationDTO;
import com.example.pharmacymanagementsystem.model.Location;
import com.example.pharmacymanagementsystem.service.LocationService;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/provinces")
    public ResponseEntity<List<LocationDTO>> getProvinces() {
        return ResponseEntity.ok(locationService.getLocationsByType(Location.LocationType.PROVINCE)
                .stream().map(LocationDTO::fromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/districts")
    public ResponseEntity<List<LocationDTO>> getDistricts(@RequestParam Long provinceId) {
        return ResponseEntity.ok(locationService.getChildrenByParentId(provinceId)
                .stream().map(LocationDTO::fromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/sectors")
    public ResponseEntity<List<LocationDTO>> getSectors(@RequestParam Long districtId) {
        return ResponseEntity.ok(locationService.getChildrenByParentId(districtId)
                .stream().map(LocationDTO::fromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/cells")
    public ResponseEntity<List<LocationDTO>> getCells(@RequestParam Long sectorId) {
        return ResponseEntity.ok(locationService.getChildrenByParentId(sectorId)
                .stream().map(LocationDTO::fromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/villages")
    public ResponseEntity<List<LocationDTO>> getVillages(@RequestParam Long cellId) {
        return ResponseEntity.ok(locationService.getChildrenByParentId(cellId)
                .stream().map(LocationDTO::fromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Long id) {
        return ResponseEntity.ok(LocationDTO.fromEntity(locationService.getLocationById(id)));
    }
}