package com.heb.togglr.api.exceptions;

public class AdminNotFoundException extends Exception {

    public AdminNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
