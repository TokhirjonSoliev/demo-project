package com.exadel.coolDesking.office;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfficeCreateDto {
    @NotBlank
    @Length(min=1, max=100)
    private String name;
    @NotBlank
    @Length(min=1, max=5)
    private String shortName;
    @NotBlank
    @Length(min=1, max=100)
    private String country;
    @NotBlank
    @Length(min=1, max=100)
    private String city;
    @NotBlank
    @Length(min=1, max=255)
    private String address;
    @NotNull
    @Min(value = 0)
    private Integer parkingCapacity;
    @NotNull
    @Positive
    private Integer floorCount;
}
