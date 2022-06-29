package com.exadel.coolDesking.user;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UserDto {
    private String firstName;
    private String username;
    private String password;
    private String lastName;
    private String email;
    private String telegramId;
    private UUID preferredWorkplace;
    private String role;
    private LocalDate employmentStart;
    private LocalDate employmentEnd;
    private String userState;
}
