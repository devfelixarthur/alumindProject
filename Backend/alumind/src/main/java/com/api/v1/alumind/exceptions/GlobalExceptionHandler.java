package com.api.v1.alumind.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleCustomRuntimeException(CustomRuntimeException ex) {
        logError("CustomRuntimeException", ex);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("statusCode", ex.getHttpStatus().value());
        response.put("error", ex.getTitle());
        response.put("timestamp", Instant.now());
        response.put("description", List.of(Map.of("message", ex.getMessage())));
        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logError("MethodArgumentNotValidException", ex);
        List<Map<String, String>> description = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            Map<String, String> errorDetails = new LinkedHashMap<>();
            errorDetails.put("field", error.getField());
            errorDetails.put("message", error.getDefaultMessage());
            description.add(errorDetails);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("statusCode", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");
        response.put("timestamp", Instant.now());
        response.put("description", description);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        logError("HttpRequestMethodNotSupportedException", ex);
        String supportedMethods = String.join(", ", ex.getSupportedHttpMethods().toString());
        String errorDescription = String.format("Method '%s' not supported. Supported methods: %s",
                ex.getMethod(), supportedMethods);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("statusCode", HttpStatus.METHOD_NOT_ALLOWED.value());
        response.put("error", "Invalid HTTP Method");
        response.put("timestamp", Instant.now());
        response.put("description", List.of(Map.of("message", errorDescription)));

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        logError("HttpMessageNotReadableException", ex);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("statusCode", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("timestamp", Instant.now());
        response.put("description", Map.of(
                "message", "The request body is missing or invalid."
        ));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Map<String, Object>> handleParameterExceptions(Exception ex) {
        logError("MissingServletRequestParameterException", ex);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("statusCode", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("timestamp", Instant.now());

        if (ex instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException missingEx = (MissingServletRequestParameterException) ex;

            response.put("description", Map.of(
                    "message", String.format("The parameter '%s' is missing and is required.", missingEx.getParameterName()),
                    "expectedType", missingEx.getParameterType()
            ));
        } else if (ex instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException typeMismatchEx = (MethodArgumentTypeMismatchException) ex;

            response.put("description", Map.of(
                    "message", String.format(
                            "The parameter '%s' received an invalid value: '%s'. Expected: '%s'.",
                            typeMismatchEx.getName(),
                            typeMismatchEx.getValue(),
                            typeMismatchEx.getRequiredType() != null ? typeMismatchEx.getRequiredType().getSimpleName() : "Unknown"
                    )
            ));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        logError("NoHandlerFoundException", ex);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("statusCode", HttpStatus.NOT_FOUND.value());
        response.put("error", "Not Found");
        response.put("timestamp", Instant.now());
        response.put("description", Map.of(
                "message", String.format("The route '%s' was not found.",
                        ex.getRequestURL(), ex.getHttpMethod())
        ));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        logError("Unhandled Exception", ex);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal Server Error");
        response.put("timestamp", Instant.now());
        response.put("description", List.of(Map.of("message", ex.getMessage())));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private void logError(String exceptionType, Exception ex) {
        logger.error("\n======== START LOG ERROR ========\n" +
                        "Exception Type: {}\n" +
                        "Message: {}\n" +
                        "Stack Trace: \n{}\n" +
                        "======== END LOG ERROR ========",
                exceptionType,
                ex.getMessage(),
                extractStackTrace(ex));
    }

    private String extractStackTrace(Exception ex) {
        StringBuilder stackTrace = new StringBuilder();
        for (StackTraceElement element : ex.getStackTrace()) {
            stackTrace.append("\tat ").append(element.toString()).append("\n");
        }
        return stackTrace.toString();
    }

}
