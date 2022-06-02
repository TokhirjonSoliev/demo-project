package com.exadel.coolDesking.floorPlan;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class FloorPlanMapper {
    private final ModelMapper mapper;

    public FloorPlanMapper() {
        this.mapper = new ModelMapper();
    }

    public FloorPlan floorPlanCreateDtoToEntity(FloorPlanCreateDTO floorPlanCreateDTO) {
        return mapper.map(floorPlanCreateDTO, FloorPlan.class);
    }

    public void floorPlanUpdateDtoToEntity(FloorPlanUpdateDTO floorPlanUpdateDTO, FloorPlan floorPlan) {
        mapper.map(floorPlanUpdateDTO, floorPlan);
    }

    public FloorPlanResponseDTO floorPlanEntityToResponseDto(FloorPlan floorPlan) {
        return mapper.map(floorPlan, FloorPlanResponseDTO.class);
    }
}
