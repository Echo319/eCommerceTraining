package com.rdavies.authservice.exceptions;

public class InactiveAccountException extends RuntimeException {
    public InactiveAccountException() {
        super("This account has been deactivated");
    }
}
