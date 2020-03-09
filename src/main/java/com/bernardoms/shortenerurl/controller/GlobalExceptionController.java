package com.bernardoms.shortenerurl.controller;

import com.bernardoms.shortenerurl.exception.AliasNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionController {

    @ExceptionHandler(Exception.class)
    private ResponseEntity<?> handleConflictException(Exception e, HttpServletRequest request) {
        HashMap<Object, Object> error = mountError(e);
        log.error("Error on processing the  request", e);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        HashMap<Object, Object> error = mountError(e);
        log.error("Error on processing the  request", e);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AliasNotFoundException.class)
    private ResponseEntity<?> handleAliasException(AliasNotFoundException e) {
        HashMap<Object, Object> error = mountError(e);
        log.error("Error on processing the  request", e);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    private HashMap<Object, Object> mountError(Exception e) {
        var error = new HashMap<>();
        error.put("error_code", "004");
        error.put("description", e.getMessage());
        return error;
    }
}
