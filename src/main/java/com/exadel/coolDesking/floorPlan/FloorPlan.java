package com.exadel.coolDesking.floorPlan;

import com.exadel.coolDesking.auditing.AbsEntity;
import com.exadel.coolDesking.office.Office;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "floor_plan", uniqueConstraints = @UniqueConstraint(columnNames = {"office_id", "floor"}))
public class FloorPlan extends AbsEntity {
    @Id
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "office_id", referencedColumnName = "id")
    private Office office;

    @NotNull
    @Min(0)
    @Column(name = "floor")
    private Integer floor;

    @NotNull
    @Column(name = "has_kitchen")
    private Boolean hasKitchen;

    @NotNull
    @Column(name = "has_conf_room")
    private Boolean hasConfRoom;

    @Override
    public String toString() {
        return "FloorPlan{" +
                "id=" + id +
                ", office=" + office +
                ", floor=" + floor +
                ", hasKitchen=" + hasKitchen +
                ", hasConfRoom=" + hasConfRoom +
                '}';
    }
}
