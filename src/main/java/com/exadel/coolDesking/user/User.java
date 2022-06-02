package com.exadel.coolDesking.user;

import com.exadel.coolDesking.workspace.Workplace;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false)
    private UUID id;

    @NotBlank
    @Length(min = 1, max = 100)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Length(min = 1, max = 100)
    @Column(name = "last_name")
    private String last_name;

    @NotBlank
    @Length(min = 3, max = 255)
    @Column(name = "email", unique = true)
    private String email;

    @NotBlank
    @Length(min = 3, max = 20)
    @Column(name = "telegram_id", unique = true)
    private String telegramId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_state")
    private UserState userState;

    @NotNull
    @Column(name = "employment_start")
    private LocalDate employmentStart;

    @Column(name = "employment_end")
    private LocalDate employmentEnd;

    @ManyToOne
    @JoinColumn(name = "preferred_workplace_id", referencedColumnName = "id")
    private Workplace preferredWorkplace;
}
