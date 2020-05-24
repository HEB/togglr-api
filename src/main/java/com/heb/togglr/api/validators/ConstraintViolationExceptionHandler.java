package com.heb.togglr.api.validators;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ConstraintViolationExceptionHandler {

    /**
     * This handles cases when entity fails @Valid check. As of 5/23/20, this is done for AppEntities.
     * Returns the group of error messages in a human-readable format.
     * @param e
     * @return
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e) {

        StringBuilder errorMessage = new StringBuilder();

        for (ConstraintViolation violation: e.getConstraintViolations()) {
            errorMessage.append(violation.getMessage());
            errorMessage.append(". ");
        }

        return new ResponseEntity<Object>(errorMessage.toString().trim(), HttpStatus.BAD_REQUEST);
    }
}
