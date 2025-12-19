package com.example.pharmacymanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pharmacymanagementsystem.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByType(Location.LocationType type);
    List<Location> findByParentId(Long parentId);
}