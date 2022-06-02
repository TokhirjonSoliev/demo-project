package com.exadel.coolDesking.office;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Setter
@Getter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "office")
public class Office {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false)
    @EqualsAndHashCode.Exclude
    private UUID id;

    @NotBlank
    @Length(min=1, max=100)
    @Column(name = "name")
    private String name;

    @NotBlank
    @Length(min=1, max=5)
    @Column(name = "short_name", unique = true)
    private String shortName;

    @NotBlank
    @Length(min=1, max=100)
    @Column(name = "country")
    private String country;

    @NotBlank
    @Length(min=1, max=100)
    @Column(name = "city")
    private String city;

    @NotBlank
    @Length(min=1, max=255)
    @Column(name = "address")
    private String address;

    @NotNull
    @Column(name = "parking_capacity")
    private Integer parkingCapacity;

    @NotNull
    @Column(name = "floor_count")
    private Integer floorCount;
}
