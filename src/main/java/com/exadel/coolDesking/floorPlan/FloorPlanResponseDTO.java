package com.exadel.coolDesking.floorPlan;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FloorPlanResponseDTO {
    @NotNull
    private UUID id;

    @NotNull
    private UUID officeId;

    @NotNull
    private Integer floor;

    @NotNull
    private Boolean hasKitchen;

    @NotNull
    private Boolean hasConfRoom;
}
