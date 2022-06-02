package com.exadel.coolDesking.report;

import com.exadel.coolDesking.booking.Booking;
import com.exadel.coolDesking.booking.BookingRepository;
import com.exadel.coolDesking.common.exception.NoContentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReportService {
    private final BookingRepository bookingRepository;
    private final ReportMapperForOnlyId mapperForOnlyId;
    private final ReportMapperForFullField mapperForFullField;

    public ReportResponseDtos<ReportResponseDtoWithUserId> getReportByOfficeWithEmptyUser(UUID officeId, LocalDate startDate, LocalDate finishDate) {
        List<Booking> bookings = bookingRepository.findAllByWorkplace_FloorPlan_Office_IdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(
                officeId, startDate, finishDate);

        if (bookings.size() == 0) {
            throw new NoContentException("There are no any bookings", Booking.class, "booking");
        }

        List<ReportResponseDtoWithUserId> responseDtos = bookings.stream().map(mapperForOnlyId::entityToResponse).toList();
        return new ReportResponseDtos(0, responseDtos);
    }

    public ReportResponseDtos<ReportResponseDtoWithFullUser> getReportByOfficeWithFullUser(UUID officeId, LocalDate startDate, LocalDate finishDate) {
        List<Booking> bookings = bookingRepository.findAllByWorkplace_FloorPlan_Office_IdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(
                officeId, startDate, finishDate);

        if (bookings.size() == 0) {
            throw new NoContentException("There are no any bookings", Booking.class, "booking");
        }

        List<ReportResponseDtoWithFullUser> responseDtos = bookings.stream().map(mapperForFullField::entityToResponse).toList();
        return new ReportResponseDtos(1, responseDtos);
    }

    public ReportResponseDtos<ReportResponseDtoWithUserId> getReportByOfficeAndFloorPlanWithEmptyUser(UUID floorPlanId, UUID officeId, LocalDate startDate, LocalDate finishDate) {
        List<Booking> bookings = bookingRepository.findAllByWorkplace_FloorPlan_IdAndWorkplace_FloorPlan_Office_IdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(
                floorPlanId, officeId, startDate, finishDate);

        if (bookings.size() == 0) {
            throw new NoContentException("There are no any bookings", Booking.class, "booking");
        }

        List<ReportResponseDtoWithUserId> responseDtos = bookings.stream().map(mapperForOnlyId::entityToResponse).toList();
        return new ReportResponseDtos(0, responseDtos);
    }

    public ReportResponseDtos<ReportResponseDtoWithFullUser> getReportByOfficeAndFloorPlanWithFullUser(UUID floorPlanId, UUID officeId, LocalDate startDate, LocalDate finishDate) {
        List<Booking> bookings = bookingRepository.findAllByWorkplace_FloorPlan_IdAndWorkplace_FloorPlan_Office_IdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(
                floorPlanId, officeId, startDate, finishDate);

        if (bookings.size() == 0) {
            throw new NoContentException("There are no any bookings", Booking.class, "booking");
        }

        List<ReportResponseDtoWithFullUser> responseDtos = bookings.stream().map(mapperForFullField::entityToResponse).toList();
        return new ReportResponseDtos(1, responseDtos);
    }
}

