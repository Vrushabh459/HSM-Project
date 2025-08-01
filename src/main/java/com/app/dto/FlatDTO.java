package com.app.dto;

import com.app.model.FlatType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FlatDTO {
    private Long id;
    
    @NotBlank(message = "Flat number is required")
    private String flatNumber;
    
    @Min(value = 1, message = "Floor number must be at least 1")
    private Integer floorNumber;
    
    private FlatType flatType;
    
    private Double area;
    
    @NotNull(message = "Building ID is required")
    private Long buildingId;
    
    private String buildingName;
    private String societyName;
}

