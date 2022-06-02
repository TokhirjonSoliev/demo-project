package com.exadel.coolDesking.floorPlan;

import com.exadel.coolDesking.office.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FloorPlanRepository extends JpaRepository<FloorPlan, UUID> {
    boolean existsByFloorAndOfficeId(Integer floor, UUID officeId);

    @Query(value = "select CAST(id as varchar) from floor_plan f where f.office_id=?", nativeQuery = true)
    List<UUID> findIdsByOfficeId(UUID officeId);

    boolean existsByFloorAndOfficeIdAndIdIsNot(Integer floor, UUID officeId, UUID id);
    @Query("SELECT f.floor FROM FloorPlan f WHERE f.office = ?1")
    List<Integer> getAllFloorsByOffice(Office office);

    Optional<FloorPlan> findFloorPlanByOfficeAndFloor(Office office, Integer floor);
}
