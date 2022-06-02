package com.exadel.coolDesking.common.exception;

import lombok.Getter;

@Getter
public class NoContentException extends RuntimeException {
    private final Class type;
    private final String search;
    public NoContentException(String message, Class type, String search) {
        super(message);
        this.type = type;
        this.search = search;
    }
}
