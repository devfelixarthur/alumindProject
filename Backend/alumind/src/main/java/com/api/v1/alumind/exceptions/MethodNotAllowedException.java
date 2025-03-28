package com.api.v1.alumind.exceptions;

import org.springframework.http.HttpStatus;

public class MethodNotAllowedException extends CustomRuntimeException {

  public MethodNotAllowedException(String message) {
    super(message);
  }

  public MethodNotAllowedException(String message, Throwable cause) {
    super(message, cause);
  }

  public MethodNotAllowedException(String title, String message) {
    super(title, message);
  }

  public MethodNotAllowedException(String message, HttpStatus httpStatus) {
    super(message, httpStatus);
  }

  public MethodNotAllowedException(String title, String message, HttpStatus httpStatus) {
    super(title, message, httpStatus);
  }

  @Override
  protected String defineTitle() {
    return "Method not Allowed";
  }

  @Override
  protected HttpStatus defineHttpStatus() {
    return HttpStatus.METHOD_NOT_ALLOWED;
  }
}

