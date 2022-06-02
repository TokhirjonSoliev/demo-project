package com.exadel.coolDesking.report;

import com.exadel.coolDesking.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserFullDto {
    private UUID id;
    private String firstName;
    private String last_name;
    private String email;
    private String telegramId;
    private String role;

}
