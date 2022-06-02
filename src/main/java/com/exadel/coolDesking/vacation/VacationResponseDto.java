package com.exadel.coolDesking.vacation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VacationResponseDto {
    @NotNull
    private UUID vacationId;

    @NotNull
    private UUID userId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate start;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;
}
