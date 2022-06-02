package com.exadel.coolDesking.vacation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VacationUpdateDto {
    @NotNull
    private LocalDate start;

    @NotNull
    private LocalDate end;
}
