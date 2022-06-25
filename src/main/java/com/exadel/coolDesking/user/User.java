package com.exadel.coolDesking.user;

import com.exadel.coolDesking.workspace.Workplace;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "`users`")
public class User implements UserDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id")
    private UUID id;

    @NotBlank
    @Length(min = 1, max = 100)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Length(min = 1, max = 100)
    @Column(name = "username")
    private String username;

    @NotBlank
    @Length(min = 8)
    @Column(name = "password")
    private String password;

    @NotBlank
    @Length(min = 1, max = 100)
    @Column(name = "last_name")
    private String lastName;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "user_state")
    private UserState userState;

    @NotNull
    @Column(name = "employment_start")
    private LocalDate employmentStart;

    @Column(name = "employment_end")
    private LocalDate employmentEnd;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "preferred_workplace_id", referencedColumnName = "id")
    private Workplace preferredWorkplace;

    @Column(name = "account_non_expired")
    private boolean accountNonExpired = true;
    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;
    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired = true;
    @Column(name = "enabled")
    private boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority("ROLE_"+this.role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
