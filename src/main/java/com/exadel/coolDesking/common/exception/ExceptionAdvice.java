package com.exadel.coolDesking.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException exception){
        log.info(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionResponse(exception.getMessage(), exception.getSearch())
        );
    }

    @ExceptionHandler(NoContentException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<ExceptionResponse> handleNoContentException(NoContentException exception){
        log.info(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                new ExceptionResponse(exception.getMessage(), exception.getSearch())
        );
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ExceptionResponse> handleConflictException(ConflictException exception){
        log.info(exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ExceptionResponse(exception.getMessage(), exception.getField())
        );
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleForbiddenException(BadRequestException exception){
        log.info(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionResponse(exception.getMessage(), exception.getField())
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionResponse> handleOtherExceptions(Exception exception){
        log.info(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ExceptionResponse(exception.getMessage(), exception.getLocalizedMessage())
        );
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleInvalidParameter(MethodArgumentNotValidException exception){
        log.info(exception.getMessage());

        List<ExceptionResponse> errorMessages = exception.getBindingResult().getFieldErrors().stream()
                .map(err -> new ExceptionResponse(err.getDefaultMessage(), err.getField()))
                .distinct()
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                errorMessages
        );
    }

}

