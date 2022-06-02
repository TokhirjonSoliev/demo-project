package com.exadel.coolDesking.common.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotFoundException extends RuntimeException {
    private Class type;
    private String search;

    public NotFoundException(String message, Class type, String search) {
        super(message);
        this.type = type;
        this.search = search;
    }
}