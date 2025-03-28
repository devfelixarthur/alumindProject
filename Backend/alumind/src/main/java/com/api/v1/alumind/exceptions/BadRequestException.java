package com.api.v1.alumind.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CustomRuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(String title, String message) {
        super(title, message);
    }

    public BadRequestException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public BadRequestException(String title, String message, HttpStatus httpStatus) {
        super(title, message, httpStatus);
    }

    @Override
    protected String defineTitle() {
        return "Bad Request";
    }

    @Override
    protected HttpStatus defineHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
