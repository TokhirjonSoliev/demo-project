package com.exadel.coolDesking.report;

import com.exadel.coolDesking.booking.Booking;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class ReportMapperForOnlyId {
    private final ModelMapper mapper;

    public ReportMapperForOnlyId() {
        this.mapper = configureModelMapper();
    }

    public ReportResponseDtoWithUserId entityToResponse(Booking booking) {
        return mapper.map(booking, ReportResponseDtoWithUserId.class);
    }

    private static ModelMapper configureModelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        PropertyMap<Booking, ReportResponseDtoWithUserId> configEndDate =
                new PropertyMap<Booking, ReportResponseDtoWithUserId>() {
                    protected void configure() {
                        map(source.getEndDate()).setFinishDate(null);
                    }
                };

        PropertyMap<Booking, ReportResponseDtoWithUserId> configFloorPlanId =
                new PropertyMap<Booking, ReportResponseDtoWithUserId>() {
                    protected void configure() {
                        map(source.getWorkplace().getFloorPlan().getId()).setFloorPlanId(null);
                    }
                };
        PropertyMap<Booking, ReportResponseDtoWithUserId> configUser =
                new PropertyMap<Booking, ReportResponseDtoWithUserId>() {
                    protected void configure() {
                        map(new UserIdDto(source.getUser().getId())).setUser(null);
                    }
                };

        PropertyMap<Booking, ReportResponseDtoWithUserId> configWorkplace =
                new PropertyMap<Booking, ReportResponseDtoWithUserId>() {
                    protected void configure() {
                        map(new WorkplaceIdDto(source.getWorkplace().getId())).setWorkplace(null);
                    }
                };

        PropertyMap<Booking, ReportResponseDtoWithUserId> configOfficeId =
                new PropertyMap<Booking, ReportResponseDtoWithUserId>() {
                    protected void configure() {
                        map(source.getWorkplace().getFloorPlan().getOffice().getId()).setOfficeId(null);
                    }
                };

        modelMapper.addMappings(configEndDate);
        modelMapper.addMappings(configFloorPlanId);
        modelMapper.addMappings(configUser);
        modelMapper.addMappings(configWorkplace);
        modelMapper.addMappings(configOfficeId);
        return modelMapper;
    }
}
