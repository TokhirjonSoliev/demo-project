package com.exadel.coolDesking.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IncompleteBookingRepository extends JpaRepository<InCompleteBooking, UUID> {
    boolean existsByCreatedById(UUID createdById);

    @Transactional
    void deleteAllByCreatedById(UUID createdById);
    Optional<InCompleteBooking> findIncompleteBookingById(UUID id);
}
