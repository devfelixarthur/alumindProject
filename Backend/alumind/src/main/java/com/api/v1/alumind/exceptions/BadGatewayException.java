package com.api.v1.alumind.exceptions;

import org.springframework.http.HttpStatus;

public class BadGatewayException extends CustomRuntimeException {

    public BadGatewayException(String message) {
        super(message);
    }

    public BadGatewayException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadGatewayException(String title, String message) {
        super(title, message);
    }

    public BadGatewayException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public BadGatewayException(String title, String message, HttpStatus httpStatus) {
        super(title, message, httpStatus);
    }

    @Override
    protected String defineTitle() {
        return "Bad Gateway";
    }

    @Override
    protected HttpStatus defineHttpStatus() {
        return HttpStatus.BAD_GATEWAY;
    }
}

