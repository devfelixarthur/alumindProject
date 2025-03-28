package com.api.v1.alumind.exceptions;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends CustomRuntimeException {

    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalServerErrorException(String title, String message) {
        super(title, message);
    }

    public InternalServerErrorException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public InternalServerErrorException(String title, String message, HttpStatus httpStatus) {
        super(title, message, httpStatus);
    }

    @Override
    protected String defineTitle() {
        return "Internal Server Error";
    }

    @Override
    protected HttpStatus defineHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
