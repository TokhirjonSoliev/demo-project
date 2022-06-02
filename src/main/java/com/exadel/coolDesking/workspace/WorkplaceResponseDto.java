package com.exadel.coolDesking.workspace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkplaceResponseDto {
    private UUID id;
    private UUID floorPlanId;
    private Integer workplaceNumber;
    private WorkplaceType type;
    private WorkplaceStatus status;
    private Boolean isNextToWindow;
    private Boolean hasPc;
    private Boolean hasMonitor;
    private Boolean hasKeyboard;
    private Boolean hasMouse;
    private Boolean hasHeadSet;

}
