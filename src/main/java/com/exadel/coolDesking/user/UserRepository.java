package com.exadel.coolDesking.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Transactional
    @Modifying
    @Query("update User u set u.userState = ?1 where u.telegramId = ?2")
    int setUserStatus(UserState userState, String telegramId);

    Optional<User> findByTelegramId(String telegramId);
}
