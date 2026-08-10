package com.pilotcoupondispatchservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MissingHeaderException extends RuntimeException {

    public MissingHeaderException(String message) {
        super(message);
    }
}
