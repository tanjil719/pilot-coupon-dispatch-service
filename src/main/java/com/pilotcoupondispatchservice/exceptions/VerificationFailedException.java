package com.pilotcoupondispatchservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class VerificationFailedException extends RuntimeException {

    public VerificationFailedException(String message) {
        super(message);
    }
}
