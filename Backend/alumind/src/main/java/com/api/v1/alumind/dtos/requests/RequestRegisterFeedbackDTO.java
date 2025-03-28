package com.api.v1.alumind.dtos.requests;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestRegisterFeedbackDTO (
        @NotBlank(message = "The feedback key is mandatory and cannot be empty.")
        @Size(min = 3, max = 500, message = "The feedback must be between 3 and 500 characters.")
        String feedback
) {
}
