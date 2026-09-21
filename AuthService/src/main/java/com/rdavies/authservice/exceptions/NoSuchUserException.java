package com.rdavies.authservice.exceptions;

public class NoSuchUserException extends RuntimeException {
    public NoSuchUserException() {
        super("No such User can be found");
    }
}
