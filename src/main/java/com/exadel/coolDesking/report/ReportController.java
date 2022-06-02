package com.exadel.coolDesking.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;

    /**
     * this method gets all bookings with only userId by officeId in the given period of time
     *
     * @param office_id   officeId
     * @param start_date  tarting date of booking
     * @param finish_date ending date of booking
     * @return ReportResponseDtos
     */
    @GetMapping("/office/{office_id}/full/0/start/{start_date}/finish/{finish_date}")
    public ResponseEntity<?> getReportByOfficeIdWithEmptyUser(
            @PathVariable UUID office_id,
            @PathVariable @Valid @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start_date,
            @PathVariable @Valid @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate finish_date
    ) {
        return ResponseEntity.ok(reportService.getReportByOfficeWithEmptyUser(office_id, start_date, finish_date));
    }

    /**
     * this method gets all bookings with full information of user by officeId in the given period of time
     *
     * @param office_id   officeId
     * @param start_date  starting date of booking
     * @param finish_date ending date of booking
     * @return ReportResponseDtos
     */
    @GetMapping("/office/{office_id}/full/1/start/{start_date}/finish/{finish_date}")
    public ResponseEntity<?> getReportByOfficeIdWithFullUser(
            @PathVariable UUID office_id,
            @PathVariable @Valid @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start_date,
            @PathVariable @Valid @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate finish_date
    ) {
        return ResponseEntity.ok(reportService.getReportByOfficeWithFullUser(office_id, start_date, finish_date));
    }

    /**
     * this method gets all bookings with only userId by officeId and floorPlanId in the given period of time
     *
     * @param office_id   officeId
     * @param floor_id    floorPlanId
     * @param start_date  starting date of booking
     * @param finish_date ending date of booking
     * @return ReportResponseDtos
     */
    @GetMapping("/office/{office_id}/floor/{floor_id}/full/0/start/{start_date}/finish/{finish_date}")
    public ResponseEntity<?> getReportByOfficeAndFloorPlanWithEmptyUser(
            @PathVariable UUID office_id,
            @PathVariable UUID floor_id,
            @PathVariable @Valid @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start_date,
            @PathVariable @Valid @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate finish_date
    ) {
        return ResponseEntity.ok(reportService.getReportByOfficeAndFloorPlanWithEmptyUser(floor_id, office_id, start_date, finish_date));
    }

    /**
     * this method gets all bookings with full information of user by officeId and floorPlanId in the given period of time
     *
     * @param office_id   officeId
     * @param floor_id    floorPlanId
     * @param start_date  starting date of booking
     * @param finish_date ending date of booking
     * @return ReportResponseDtos
     */
    @GetMapping("/office/{office_id}/floor/{floor_id}/full/1/start/{start_date}/finish/{finish_date}")
    public ResponseEntity<?> getReportByOfficeAndFloorPlanWithFullUser(
            @PathVariable UUID office_id,
            @PathVariable UUID floor_id,
            @PathVariable @Valid @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start_date,
            @PathVariable @Valid @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate finish_date
    ) {
        return ResponseEntity.ok(reportService.getReportByOfficeAndFloorPlanWithFullUser(floor_id, office_id, start_date, finish_date));
    }
}
