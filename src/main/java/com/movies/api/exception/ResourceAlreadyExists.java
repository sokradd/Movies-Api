package com.movies.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource Already Exists")
public class ResourceAlreadyExists extends Exception {

    // custom error constructor
    public ResourceAlreadyExists(String message) {
        super(message);
    }
}
