package com.exadel.coolDesking.user;

import com.exadel.coolDesking.config.auth.ApplicationUser;

import java.util.Optional;

public interface UserDao {
    Optional<ApplicationUser> selectApplicationUserByUsername(String username);
}
