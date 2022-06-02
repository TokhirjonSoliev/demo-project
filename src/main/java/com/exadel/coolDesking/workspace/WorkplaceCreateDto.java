package com.exadel.coolDesking.workspace;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkplaceCreateDto {
    @NotNull
    private Integer workplaceNumber;

    @NotNull
    private WorkplaceType type;

    @NotNull
    private Boolean isNextToWindow;

    @NotNull
    private Boolean hasPc;

    @NotNull
    private Boolean hasMonitor;

    @NotNull
    private Boolean hasKeyboard;

    @NotNull
    private Boolean hasMouse;

    @NotNull
    private Boolean hasHeadSet;

}
