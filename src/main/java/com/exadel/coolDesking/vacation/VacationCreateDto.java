package com.exadel.coolDesking.vacation;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VacationCreateDto {
    @NotNull
    private UUID userId;
    @NotNull
    private LocalDate start;
    @NotNull
    private LocalDate end;
}
