package com.exadel.coolDesking.office;

import com.exadel.coolDesking.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OfficeRepository extends JpaRepository<Office, UUID> {
    boolean existsByName(String name);

    @Query("SELECT DISTINCT country FROM Office")
    List<String> findAllCountries();

    @Query("SELECT DISTINCT o.city FROM Office o WHERE o.country = ?1")
    List<String> findAllCitiesByCountry(String country);

    @Query("SELECT o.name FROM Office o WHERE o.city = ?1")
    List<String> findOfficeNamesByCity(String city);

    Optional<Office> getOfficeByName(String name);

    @Query(value = "SELECT DISTINCT u from Office o inner join FloorPlan f on ?1=f.office.id" +
            " inner join Workplace w on w.floorPlan.id=f.id" +
            " inner join User u on u.preferredWorkplace.id=w.id")
    List<User> findUserOfficeFloorPlanWorkplace(UUID officeId);
}
