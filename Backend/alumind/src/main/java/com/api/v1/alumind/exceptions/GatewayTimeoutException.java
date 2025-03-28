package com.api.v1.alumind.exceptions;

import org.springframework.http.HttpStatus;

public class GatewayTimeoutException extends CustomRuntimeException {

    public GatewayTimeoutException(String message) {
        super(message);
    }

    public GatewayTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public GatewayTimeoutException(String title, String message) {
        super(title, message);
    }

    public GatewayTimeoutException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public GatewayTimeoutException(String title, String message, HttpStatus httpStatus) {
        super(title, message, httpStatus);
    }

    @Override
    protected String defineTitle() {
        return "Gateway TimeOut";
    }

    @Override
    protected HttpStatus defineHttpStatus() {
        return HttpStatus.GATEWAY_TIMEOUT;
    }
}

