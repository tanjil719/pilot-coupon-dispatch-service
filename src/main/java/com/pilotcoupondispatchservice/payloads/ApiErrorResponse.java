package com.pilotcoupondispatchservice.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApiErrorResponse {

    private HttpStatus status;
    private Date timestamp;
    private String message;
    private List<String> errors;
    private String path;

    public ApiErrorResponse(HttpStatus status, Date timestamp, String message, String error, String path) {
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
        this.errors = Collections.singletonList(error);
        this.path = path;
    }

    public ApiErrorResponse(HttpStatus status, Date timestamp, String message, List<String> errors, String path) {
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
        this.errors = errors;
        this.path = path;
    }
}
