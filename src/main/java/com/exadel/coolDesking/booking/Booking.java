package com.exadel.coolDesking.booking;

import com.exadel.coolDesking.user.User;
import com.exadel.coolDesking.workspace.Workplace;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "workplace_id", referencedColumnName = "id")
    private Workplace workplace;

    @NotNull
    @Column(name = "has_parking")
    private Boolean hasParking;

    @NotNull
    @Column(name = "start_date")
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull
    @Column(name = "frequency")
    @Enumerated(EnumType.STRING)
    private BookingFrequency frequency;
}
