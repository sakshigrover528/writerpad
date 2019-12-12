package com.xebia.fs101.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class SameContentExistsException extends RuntimeException {
    public SameContentExistsException(String message) {
        super(message);
    }
}
