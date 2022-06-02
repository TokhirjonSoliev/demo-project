package com.exadel.coolDesking.workspace;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class WorkplaceMapper {
    private final ModelMapper mapper;

    public WorkplaceMapper() {
        this.mapper = new ModelMapper();
    }

    public WorkplaceResponseDto entityToResponseDTO(Workplace workplace) {
        return mapper.map(workplace, WorkplaceResponseDto.class);
    }

    public Workplace workplaceCreateDtoToEntity(WorkplaceCreateDto workplaceCreateDto){
        return mapper.map(workplaceCreateDto, Workplace.class);
    }

    public void updateEntity(WorkplaceUpdateDto workplaceUpdateDto, Workplace workplace){
        mapper.map(workplaceUpdateDto, workplace);
    }

}
