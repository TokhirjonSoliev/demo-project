package com.exadel.coolDesking.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findAllByWorkplace_FloorPlan_Office_IdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(
            UUID workplace_floorPlan_office_id,
            LocalDate startDate,
            LocalDate endDate);

    List<Booking> findAllByWorkplace_FloorPlan_IdAndWorkplace_FloorPlan_Office_IdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(
            UUID workplace_floorPlan_id,
            UUID workplace_floorPlan_office_id,
            LocalDate startDate,
            LocalDate endDate);

    List<Booking> findBookingByStartDateBetweenAndEndDateBetween(LocalDate startDate, LocalDate startDate2, LocalDate endDate, LocalDate endDate2);
}
