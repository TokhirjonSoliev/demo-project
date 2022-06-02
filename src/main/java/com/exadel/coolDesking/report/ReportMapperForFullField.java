package com.exadel.coolDesking.report;

import com.exadel.coolDesking.booking.Booking;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class ReportMapperForFullField {
    private final ModelMapper mapper;

    public ReportMapperForFullField() {
        this.mapper = configureModelMapper();
    }

    public ReportResponseDtoWithFullUser entityToResponse(Booking booking) {
        return mapper.map(booking, ReportResponseDtoWithFullUser.class);
    }

    private static ModelMapper configureModelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        PropertyMap<Booking, ReportResponseDtoWithFullUser> configEndDate =
                new PropertyMap<Booking, ReportResponseDtoWithFullUser>() {
                    protected void configure() {
                        map(source.getEndDate()).setFinishDate(null);
                    }
                };

        PropertyMap<Booking, ReportResponseDtoWithFullUser> configFloorPlanId =
                new PropertyMap<Booking, ReportResponseDtoWithFullUser>() {
                    protected void configure() {
                        map(source.getWorkplace().getFloorPlan().getId()).setFloorPlanId(null);
                    }
                };

        PropertyMap<Booking, ReportResponseDtoWithFullUser> configWorkplace =
                new PropertyMap<Booking, ReportResponseDtoWithFullUser>() {
                    protected void configure() {
                        map(new WorkplaceIdDto(source.getWorkplace().getId())).setWorkplace(null);
                    }
                };

        PropertyMap<Booking, ReportResponseDtoWithFullUser> configOfficeId =
                new PropertyMap<Booking, ReportResponseDtoWithFullUser>() {
                    protected void configure() {
                        map(source.getWorkplace().getFloorPlan().getOffice().getId()).setOfficeId(null);
                    }
                };

        modelMapper.addMappings(configEndDate);
        modelMapper.addMappings(configFloorPlanId);
        modelMapper.addMappings(configWorkplace);
        modelMapper.addMappings(configOfficeId);
        return modelMapper;
    }
}
