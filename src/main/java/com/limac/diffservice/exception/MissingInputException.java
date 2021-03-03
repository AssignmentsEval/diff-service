package com.limac.diffservice.exception;

/**
 * Exception to be thrown when any input is missing when the comparison is being executed.
 */
public class MissingInputException extends RuntimeException {

    public MissingInputException(String message) {
        super(message);
    }
}
