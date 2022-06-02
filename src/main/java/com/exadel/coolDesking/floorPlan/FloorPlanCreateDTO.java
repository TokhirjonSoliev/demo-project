package com.exadel.coolDesking.floorPlan;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FloorPlanCreateDTO {
    @NotNull
    private UUID officeId;

    @NotNull
    @Min(0)
    private Integer floor;

    @NotNull
    private Boolean hasKitchen;

    @NotNull
    private Boolean hasConfRoom;
}
