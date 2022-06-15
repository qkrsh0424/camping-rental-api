package com.camping_rental.server.domain.exception.dto;

/**
 * 401
 */
public class InvalidUserException extends RuntimeException{
    public InvalidUserException() {
        super();
    }
    public InvalidUserException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidUserException(String message) {
        super(message);
    }
    public InvalidUserException(Throwable cause) {
        super(cause);
    }
}
