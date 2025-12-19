package com.example.pharmacymanagementsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pharmacymanagementsystem.model.Location;
import com.example.pharmacymanagementsystem.repository.LocationRepository;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public List<Location> getLocationsByType(Location.LocationType type) {
        return locationRepository.findByType(type);
    }

    public List<Location> getChildrenByParentId(Long parentId) {
        return locationRepository.findByParentId(parentId);
    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id).orElse(null);
    }
}