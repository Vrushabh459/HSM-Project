package com.app.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


import com.app.dto.FlatDTO;
import com.app.model.Flat;

@Mapper(componentModel = "spring")
public interface FlatMapper {
    
    @Mapping(source = "flat", target = "buildingId", qualifiedByName = "getBuildingId")
    @Mapping(source = "flat", target = "buildingName", qualifiedByName = "getBuildingName")
    @Mapping(source = "flat", target = "societyName", qualifiedByName = "getSocietyName")
    FlatDTO toDTO(Flat flat);
    
    List<FlatDTO> toDtoList(List<Flat> flats);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "building", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "maintenanceBills", ignore = true)
    @Mapping(target = "flatAllocations", ignore = true)
    @Mapping(target = "complaints", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Flat toEntity(FlatDTO dto);
    
    @Named("getBuildingId")
    default Long getBuildingId(Flat flat) {
        return flat != null && flat.getBuilding() != null ? flat.getBuilding().getId() : null;
    }
    
    @Named("getBuildingName")
    default String getBuildingName(Flat flat) {
        return flat != null && flat.getBuilding() != null ? flat.getBuilding().getName() : null;
    }
    
    @Named("getSocietyName")
    default String getSocietyName(Flat flat) {
        return flat != null && flat.getBuilding() != null && 
               flat.getBuilding().getSociety() != null ? flat.getBuilding().getSociety().getName() : null;
    }
}
