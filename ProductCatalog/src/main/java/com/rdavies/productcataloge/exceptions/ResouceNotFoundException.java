package com.rdavies.productcataloge.exceptions;

/**
 * Backward-compatible shim for the misspelled exception name.
 * Prefer using ResourceNotFoundException going forward.
 */
@Deprecated
public class ResouceNotFoundException extends ResourceNotFoundException {
    public ResouceNotFoundException(String message) {
        super(message);
    }
}
