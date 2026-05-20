package com.jospin.gestion_stock.validations;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
@Scope("prototype")
public class ValidationHandle {

    private static final Pattern CODE_PATTERN = Pattern.compile("^[A-Z0-9-_]+$");

    private final List<ValidationException.ValidationError> errors = new ArrayList<>();

    public ValidationHandle required(String field, Object value) {
        if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
            errors.add(new ValidationException.ValidationError(
                    field,
                    ValidationErrorType.FIELD_REQUIRED,
                    "Le champ " + field + " est obligatoire"
            ));
        }
        return this;
    }

    public ValidationHandle minLength(String field, String value, int min) {
        if (value != null && value.length() < min) {
            errors.add(new ValidationException.ValidationError(
                    field,
                    ValidationErrorType.FIELD_TOO_SHORT,
                    field + " doit contenir au moins " + min + " caractères"
            ));
        }
        return this;
    }

    public ValidationHandle maxLength(String field, String value, int max) {
        if (value != null && value.length() > max) {
            errors.add(new ValidationException.ValidationError(
                    field,
                    ValidationErrorType.FIELD_TOO_LONG,
                    field + " ne doit pas dépasser " + max + " caractères"
            ));
        }
        return this;
    }

    public ValidationHandle validCode(String field, String code) {
        if (code != null && !CODE_PATTERN.matcher(code).matches()) {
            errors.add(new ValidationException.ValidationError(
                    field,
                    ValidationErrorType.CODE_INVALID,
                    "Le code doit contenir uniquement des lettres majuscules, chiffres, tirets et underscores"
            ));
        }
        return this;
    }

    public ValidationHandle min(String field, Number value, Number min) {
        if (value != null && value.doubleValue() < min.doubleValue()) {
            errors.add(new ValidationException.ValidationError(
                    field,
                    ValidationErrorType.FIELD_INVALID,
                    field + " doit être au minimum " + min
            ));
        }
        return this;
    }

    public ValidationHandle minDecimal(String field, BigDecimal value, BigDecimal min) {
        if (value != null && value.compareTo(min) < 0) {
            errors.add(new ValidationException.ValidationError(
                    field,
                    ValidationErrorType.INVALID_PRICE,
                    field + " doit être au minimum " + min
            ));
        }
        return this;
    }

    public ValidationHandle positive(String field, Integer value) {
        if (value != null && value <= 0) {
            errors.add(new ValidationException.ValidationError(
                    field,
                    ValidationErrorType.INVALID_QUANTITY,
                    field + " doit être positif"
            ));
        }
        return this;
    }

    public ValidationHandle custom(String field, boolean isValid, ValidationErrorType errorType, String message) {
        if (!isValid) {
            errors.add(new ValidationException.ValidationError(field, errorType, message));
        }
        return this;
    }

    public void validate() {
        if (!errors.isEmpty()) {
            List<ValidationException.ValidationError> errorsCopy = new ArrayList<>(errors);
            errors.clear();
            throw new ValidationException("Échec de validation", errorsCopy);
        }
    }

    public List<ValidationException.ValidationError> getErrors() {
        return new ArrayList<>(errors);
    }

    public void clear() {
        errors.clear();
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
