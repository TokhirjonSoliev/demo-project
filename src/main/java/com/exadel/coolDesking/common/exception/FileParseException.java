package com.exadel.coolDesking.common.exception;

import lombok.Getter;

@Getter
public class FileParseException extends RuntimeException{
    private final Class type;
    private final String field;

    public FileParseException(String message, Class type, String field) {
        super(message);
        this.type = type;
        this.field = field;
    }
}
