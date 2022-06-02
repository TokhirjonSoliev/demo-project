package com.exadel.coolDesking.office;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OfficeMapper {
    private final ModelMapper modelMapper;

    private OfficeMapper() {
        this.modelMapper = new ModelMapper();
    }

    protected Office officeDtoToOfficeEntity(OfficeCreateDto officeCreateDto){
        return modelMapper.map(officeCreateDto, Office.class);
    }

    protected OfficeResponse officeToOfficeResponse(Office office){
        return modelMapper.map(office, OfficeResponse.class);
    }
}
