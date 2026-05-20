package com.jospin.gestion_stock.validations;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationException extends RuntimeException {

    private final List<ValidationError> errors;

    public ValidationException(String message) {
        super(message);
        this.errors = new ArrayList<>();
    }

    public ValidationException(String message, List<ValidationError> errors) {
        super(message);
        this.errors = errors;
    }

    public ValidationException(ValidationErrorType errorType) {
        super(errorType.getMessage());
        this.errors = List.of(new ValidationError(errorType));
    }

    public ValidationException(ValidationErrorType errorType, String customMessage) {
        super(customMessage);
        this.errors = List.of(new ValidationError(errorType, customMessage));
    }

    public ValidationException(String field, ValidationErrorType errorType) {
        super(errorType.getMessage());
        this.errors = List.of(new ValidationError(field, errorType));
    }

    public ValidationException(String field, ValidationErrorType errorType, String customMessage) {
        super(customMessage);
        this.errors = List.of(new ValidationError(field, errorType, customMessage));
    }

    @Getter
    public static class ValidationError {
        private final String field;
        private final String code;
        private final String message;

        public ValidationError(ValidationErrorType errorType) {
            this.field = null;
            this.code = errorType.getCode();
            this.message = errorType.getMessage();
        }

        public ValidationError(ValidationErrorType errorType, String customMessage) {
            this.field = null;
            this.code = errorType.getCode();
            this.message = customMessage;
        }

        public ValidationError(String field, ValidationErrorType errorType) {
            this.field = field;
            this.code = errorType.getCode();
            this.message = errorType.getMessage();
        }

        public ValidationError(String field, ValidationErrorType errorType, String customMessage) {
            this.field = field;
            this.code = errorType.getCode();
            this.message = customMessage;
        }
    }
}
