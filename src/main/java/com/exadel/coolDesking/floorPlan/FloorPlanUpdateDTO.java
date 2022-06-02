package com.exadel.coolDesking.floorPlan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FloorPlanUpdateDTO {
    @NotNull
    @Min(0)
    private Integer floor;

    @NotNull
    private Boolean hasKitchen;

    @NotNull
    private Boolean hasConfRoom;
}
