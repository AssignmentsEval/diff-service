package com.limac.diffservice.exception;

/**
 * Exception to be thrown when a resource is not found.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
