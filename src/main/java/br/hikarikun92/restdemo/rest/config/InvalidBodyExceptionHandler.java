package br.hikarikun92.restdemo.rest.config;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class InvalidBodyExceptionHandler {

    //In case a bean validation has failed for a given object, this method will be called and convert the default error
    //response to a list of key-value objects, the key being the field name and the value being the error message thrown
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        final List<RestFieldError> errors = convertToRestFieldErrors(fieldErrors);

        return ResponseEntity.badRequest().body(errors);
    }

    private List<RestFieldError> convertToRestFieldErrors(List<FieldError> fieldErrors) {
        final List<RestFieldError> errors = new ArrayList<>(fieldErrors.size());
        for (FieldError fieldError : fieldErrors) {
            final RestFieldError error = new RestFieldError(fieldError.getField(), fieldError.getDefaultMessage());
            errors.add(error);
        }

        return errors;
    }
}
