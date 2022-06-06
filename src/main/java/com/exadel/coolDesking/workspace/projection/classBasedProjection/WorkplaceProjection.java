package com.exadel.coolDesking.workspace.projection.classBasedProjection;

import com.exadel.coolDesking.workspace.WorkplaceType;

public class WorkplaceProjection {
    private Integer workplaceNumber;
    private WorkplaceType type;

    public WorkplaceProjection(Integer workplaceNumber, WorkplaceType type) {
        this.workplaceNumber = workplaceNumber;
        this.type = type;
    }

    public Integer getWorkplaceNumber() {
        return workplaceNumber;
    }

    public WorkplaceType getType() {
        return type;
    }
}
