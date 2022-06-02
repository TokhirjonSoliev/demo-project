package com.exadel.coolDesking.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WaitingListRepository extends JpaRepository<WaitingList, UUID> {
}
