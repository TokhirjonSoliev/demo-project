package com.exadel.coolDesking.workspace;

import com.exadel.coolDesking.floorPlan.FloorPlan;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "workplace", uniqueConstraints
        = @UniqueConstraint(columnNames = {"workplace_number", "floor_plan_id"}))
public class Workplace {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "floor_plan_id", referencedColumnName = "id")
    private FloorPlan floorPlan;

    @NotNull
    @Column(name = "workplace_number")
    private Integer workplaceNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private WorkplaceType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WorkplaceStatus status;

    @NotNull
    @Column(name = "has_pc")
    private Boolean hasPc;

    @NotNull
    @Column(name = "has_keyboard")
    private Boolean hasKeyboard;

    @NotNull
    @Column(name = "has_monitor")
    private Boolean hasMonitor;

    @NotNull
    @Column(name = "has_mouse")
    private Boolean hasMouse;

    @NotNull
    @Column(name = "has_headset")
    private Boolean hasHeadSet;

    @NotNull
    @Column(name = "is_next_to_window")
    private Boolean isNextToWindow;

    @Override
    public String toString() {
        return "Workplace{" +
                "id=" + id +
                ", floorPlan=" + floorPlan +
                ", workplaceNumber=" + workplaceNumber +
                ", type=" + type +
                ", status=" + status +
                ", hasPc=" + hasPc +
                ", hasKeyboard=" + hasKeyboard +
                ", hasMonitor=" + hasMonitor +
                ", hasMouse=" + hasMouse +
                ", hasHeadSet=" + hasHeadSet +
                ", isNextToWindow=" + isNextToWindow +
                '}';
    }
}
