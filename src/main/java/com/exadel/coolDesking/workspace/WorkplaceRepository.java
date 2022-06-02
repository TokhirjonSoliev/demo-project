package com.exadel.coolDesking.workspace;


import com.exadel.coolDesking.floorPlan.FloorPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkplaceRepository extends JpaRepository<Workplace, UUID>, JpaSpecificationExecutor<Workplace> {

    List<Workplace> findByFloorPlan_Office_Id(UUID officeId);

    Optional<Workplace> findByFloorPlan_Office_IdAndId(UUID officeId, UUID id);

    boolean existsByFloorPlan_Office_IdAndWorkplaceNumber(UUID officeId, Integer workplaceNumber);

    @Query("SELECT w.workplaceNumber FROM Workplace w WHERE w.floorPlan = ?1")
    List<Integer> findAllByFloorPlan(FloorPlan floorPlan);

    Optional<Workplace> findWorkplaceByFloorPlanAndWorkplaceNumber(FloorPlan floorPlan, Integer workplaceNumber);
}
