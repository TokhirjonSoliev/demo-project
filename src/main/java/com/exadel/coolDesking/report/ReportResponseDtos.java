package com.exadel.coolDesking.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportResponseDtos<T> {
    private Integer full;
    private List<T> responseDtos;
}
