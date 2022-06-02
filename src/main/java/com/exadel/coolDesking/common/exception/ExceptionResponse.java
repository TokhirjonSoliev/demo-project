package com.exadel.coolDesking.common.exception;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExceptionResponse {
    private String field;
    private String errorMessage;

    public ExceptionResponse(String search, String message) {
        this.field = message;
        this.errorMessage = search;
    }
}
