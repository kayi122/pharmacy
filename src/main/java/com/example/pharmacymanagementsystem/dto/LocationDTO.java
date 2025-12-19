package com.example.pharmacymanagementsystem.dto;

import com.example.pharmacymanagementsystem.model.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    private Long id;
    private String name;
    private String code;
    private String type;
    private Long parentId;

    public static LocationDTO fromEntity(Location location) {
        return LocationDTO.builder()
                .id(location.getId())
                .name(location.getName())
                .code(location.getCode())
                .type(location.getType().name())
                .parentId(location.getParent() != null ? location.getParent().getId() : null)
                .build();
    }
}
