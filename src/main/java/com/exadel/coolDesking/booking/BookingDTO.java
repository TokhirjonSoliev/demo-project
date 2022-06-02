package com.exadel.coolDesking.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingDTO {

    private UUID id;

    @NotNull
    private UUID workplaceId;

    @NotNull
    private UUID userId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private BookingFrequency frequency;

    @NotNull
    private Boolean hasParking;
}
