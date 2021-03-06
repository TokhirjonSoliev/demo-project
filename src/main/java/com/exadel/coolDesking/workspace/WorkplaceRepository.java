package com.exadel.coolDesking.workspace;


import com.exadel.coolDesking.floorPlan.FloorPlan;
import com.exadel.coolDesking.workspace.projection.classBasedProjection.WorkplaceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkplaceRepository extends JpaRepository<Workplace, UUID>, JpaSpecificationExecutor<Workplace> {

    Optional<Workplace> findByFloorPlan_Office_IdAndId(UUID officeId, UUID id);

    List<WorkplaceResponseDto> findAllByFloorPlan_Office_Id(UUID floorPlan_office_id);

    boolean existsByFloorPlan_Office_IdAndWorkplaceNumber(UUID officeId, Integer workplaceNumber);

    @Query("SELECT w.workplaceNumber FROM Workplace w WHERE w.floorPlan = ?1")
    List<Integer> findAllByFloorPlan(FloorPlan floorPlan);

    /*@Query(value = "select w.workplace_number as workPlaceNumber, w.type from workplace w \n" +
            "where w.floor_plan_id = ?1", nativeQuery = true)
    List<WorkplaceProjection> findAllByFloorPlan_Id(UUID floorPlanId);*/

    @Query(value = "select new com.exadel.coolDesking.workspace.projection.classBasedProjection.WorkplaceProjection(w.workplaceNumber, w.type) " +
            "from Workplace w where w.floorPlan.id = ?1")
    List<WorkplaceProjection> findAllByFloorPlan_Id(UUID floorPlanId);

}
