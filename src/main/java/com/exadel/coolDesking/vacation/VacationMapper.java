package com.exadel.coolDesking.vacation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class VacationMapper {
    private final ModelMapper mapper;

    public VacationMapper() {
        this.mapper = new ModelMapper();
    }

    public VacationResponseDto entityToResponseDto(Vacation vacation) {
        return mapper.map(vacation, VacationResponseDto.class);
    }

    public Vacation vacationCreateDtoToEntity(VacationCreateDto vacationCreateDto) {
        return mapper.map(vacationCreateDto, Vacation.class);
    }

    public void vacationUpdateDtoToEntity(VacationUpdateDto vacationUpdateDto,Vacation vacation) {
       mapper.map(vacationUpdateDto,vacation);
    }

}
