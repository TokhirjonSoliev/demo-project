package com.exadel.coolDesking.booking;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum BookingFrequency {
    RECURRING,
    ONE_DAY,
    CONTINUES;

    public static List<String> getList() {
        return Stream.of(values())
                .map(BookingFrequency::name)
                .collect(Collectors.toList());
    }
}
