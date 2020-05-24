package com.heb.togglr.api.validators;

import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * This handles cases when entity bodies fail checks in a validator class.
     * Returns the group of error messages in a human-readable format.
     * @param ex
     * @return
     */
    @ExceptionHandler({RepositoryConstraintViolationException.class})
    public ResponseEntity<Object> handleEntityValidationExceptions(Exception ex) {
        RepositoryConstraintViolationException repositoryConstraintViolationException = (RepositoryConstraintViolationException) ex;

        List<FieldError> errors = repositoryConstraintViolationException.getErrors().getFieldErrors();

        StringBuilder errorMessage = new StringBuilder();

        for (FieldError error: errors) {
            errorMessage.append(error.getCode());
            errorMessage.append(". ");
        }

        return new ResponseEntity<Object>(errorMessage.toString().trim(), HttpStatus.BAD_REQUEST);
    }
}
