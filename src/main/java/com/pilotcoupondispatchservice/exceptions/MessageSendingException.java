package com.pilotcoupondispatchservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MessageSendingException extends RuntimeException {

    public MessageSendingException(String message) {
        super(message);
    }
}
