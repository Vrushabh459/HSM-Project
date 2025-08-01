package com.app.service;

import java.util.List;



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.Exception.ResourceAlreadyExistsException;
import com.app.Exception.ResourceNotFoundException;
import com.app.Mapper.FlatMapper;
import com.app.dao.BuildingDao;
import com.app.dao.FlatDao;
import com.app.dto.FlatDTO;
import com.app.model.Building;
import com.app.model.Flat;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlatServiceImpl implements FlatService {

    private final FlatDao flatRepository;
    private final BuildingDao buildingRepository;
    private final FlatMapper flatMapper;

    @Override
    @Transactional(readOnly = true)
    public List<FlatDTO> getAllFlats() {
        List<Flat> flats = flatRepository.findAll();
        return flatMapper.toDtoList(flats);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FlatDTO> getFlatsByBuildingId(Long buildingId) {
        if (!buildingRepository.existsById(buildingId)) {
            throw new ResourceNotFoundException("Building not found with id: " + buildingId);
        }
        List<Flat> flats = flatRepository.findByBuildingId(buildingId);
        return flatMapper.toDtoList(flats);
    }
    @Override
    @Transactional(readOnly = true)
    public List<FlatDTO> getFlatsBySocietyId(Long societyId) {
        List<Flat> flats = flatRepository.findBySocietyId(societyId);
        return flatMapper.toDtoList(flats);
    }

    @Override
    @Transactional(readOnly = true)
    public FlatDTO getFlatById(Long id) {
        Flat flat = flatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flat not found with id: " + id));
        return flatMapper.toDTO(flat);
    }

    @Override
    @Transactional
    public FlatDTO createFlat(FlatDTO flatDto) {
        Building building = buildingRepository.findById(flatDto.getBuildingId())
                .orElseThrow(() -> new ResourceNotFoundException("Building not found with id: " + flatDto.getBuildingId()));

        // Check if flat with same number already exists in the building
        if (flatRepository.existsByFlatNumberAndBuildingId(flatDto.getFlatNumber(), flatDto.getBuildingId())) {
            throw new ResourceAlreadyExistsException("Flat already exists with number: " + flatDto.getFlatNumber() + " in building: " + building.getName());
        }

        Flat flat = Flat.builder()
                .flatNumber(flatDto.getFlatNumber())
                .floorNumber(flatDto.getFloorNumber())
                .building(building)
                .build();

        Flat savedFlat = flatRepository.save(flat);
        return flatMapper.toDTO(savedFlat);
    }
    @Override
    @Transactional
    public FlatDTO updateFlat(Long id, FlatDTO flatDto) {
        Flat flat = flatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flat not found with id: " + id));

        Building building = buildingRepository.findById(flatDto.getBuildingId())
                .orElseThrow(() -> new ResourceNotFoundException("Building not found with id: " + flatDto.getBuildingId()));

        // Check if flat number is being changed and if it already exists in the building
        if (!flat.getFlatNumber().equals(flatDto.getFlatNumber()) && 
                flatRepository.existsByFlatNumberAndBuildingId(flatDto.getFlatNumber(), flatDto.getBuildingId())) {
            throw new ResourceAlreadyExistsException("Flat already exists with number: " + flatDto.getFlatNumber() + " in building: " + building.getName());
        }

        flat.setFlatNumber(flatDto.getFlatNumber());
        flat.setFloorNumber(flatDto.getFloorNumber());
        flat.setBuilding(building);

        Flat updatedFlat = flatRepository.save(flat);
        return flatMapper.toDTO(updatedFlat);
    }

    @Override
    @Transactional
    public void deleteFlat(Long id) {
        if (!flatRepository.existsById(id)) {
            throw new ResourceNotFoundException("Flat not found with id: " + id);
        }
        flatRepository.deleteById(id);
    }
}
