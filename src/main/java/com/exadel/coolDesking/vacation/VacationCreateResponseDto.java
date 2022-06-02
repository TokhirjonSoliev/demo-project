package com.exadel.coolDesking.vacation;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VacationCreateResponseDto {
    private UUID vacationId;
}
