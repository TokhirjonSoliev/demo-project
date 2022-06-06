package com.exadel.coolDesking.workspace.projection.interfaceBasedProjection;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;


public interface WorkplaceProjection {
//    @Value("#{@workplaceRepository.findAllByFloorPlan_Id(target.floorPlanId)}")
    Integer getWorkplaceNumber();
    String getType();
}
