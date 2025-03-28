package com.api.v1.alumind.exceptions;

import org.springframework.http.HttpStatus;

public abstract class CustomRuntimeException extends RuntimeException {

  private final String title;
  private final HttpStatus httpStatus;

  protected CustomRuntimeException(String message) {
    super(message);
    this.title = defineTitle();
    this.httpStatus = defineHttpStatus();
  }

  protected CustomRuntimeException(String message, Throwable cause) {
    super(message, cause);
    this.title = defineTitle();
    this.httpStatus = defineHttpStatus();
  }

  protected CustomRuntimeException(String title, String message) {
    super(message);
    this.title = title;
    this.httpStatus = defineHttpStatus();
  }

  protected CustomRuntimeException(String message, HttpStatus httpStatus) {
    super(message);
    this.title = defineTitle();
    this.httpStatus = httpStatus;
  }

  protected CustomRuntimeException(String title, String message, HttpStatus httpStatus) {
    super(message);
    this.title = title;
    this.httpStatus = httpStatus;
  }

  protected abstract String defineTitle();

  protected abstract HttpStatus defineHttpStatus();

  public String getTitle() {
    return title;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
