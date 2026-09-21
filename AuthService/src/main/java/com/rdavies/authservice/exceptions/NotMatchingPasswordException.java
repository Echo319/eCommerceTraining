package com.rdavies.authservice.exceptions;

public class NotMatchingPasswordException extends RuntimeException {
    public NotMatchingPasswordException() {
        super("Given password does not match");
    }
}
