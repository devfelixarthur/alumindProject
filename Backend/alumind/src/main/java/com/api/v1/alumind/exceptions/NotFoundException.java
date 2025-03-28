package com.api.v1.alumind.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomRuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(String title, String message) {
        super(title, message);
    }

    public NotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public NotFoundException(String title, String message, HttpStatus httpStatus) {
        super(title, message, httpStatus);
    }

    @Override
    protected String defineTitle() {
        return "Not Found";
    }

    @Override
    protected HttpStatus defineHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
