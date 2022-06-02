package com.exadel.coolDesking.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class ReportBaseResponseDto<T, R> {
    private UUID id;
    private UUID floorPlanId;
    private UUID officeId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate finishDate;
    private String frequency;
    private T user;
    private R workplace;
}
