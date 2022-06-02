package com.exadel.coolDesking.booking;

import com.exadel.coolDesking.user.User;
import com.exadel.coolDesking.workspace.Workplace;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "waiting_list")
public class WaitingList {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false)
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "workplace_id", referencedColumnName = "id")
    private Workplace workplace;

    @NotNull
    @Column(name = "has_parking")
    private Boolean hasParking;

    @NotNull
    @Column(name = "is_recurring")
    private Boolean isRecurring;

    @Column(name = "frequency")
    private Integer frequency;

    @NotNull
    @Column(name = "start")
    private LocalDate start;

    @NotNull
    @Column(name = "end")
    private LocalDate end;
}
