package com.exadel.coolDesking.booking;

import com.exadel.coolDesking.floorPlan.FloorPlan;
import com.exadel.coolDesking.office.Office;
import com.exadel.coolDesking.user.User;
import com.exadel.coolDesking.workspace.Workplace;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "incomplete_booking")
public class InCompleteBooking {
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
    @JoinColumn(name = "office_id", referencedColumnName = "id")
    private Office office;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "created_by_id", unique = true, referencedColumnName = "id")
    private User createdBy;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "created_for_id", referencedColumnName = "id")
    private User createdFor;

    @ManyToOne
    @JoinColumn(name = "floor_plan_id", referencedColumnName = "id")
    private FloorPlan floorPlan;

    @ManyToOne
    @JoinColumn(name = "workplace_id", referencedColumnName = "id")
    private Workplace workplace;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency")
    private BookingFrequency frequency;

    @Column(name = "has_parking")
    private Boolean hasParking;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
}
