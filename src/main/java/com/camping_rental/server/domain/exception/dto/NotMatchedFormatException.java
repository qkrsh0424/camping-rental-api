package com.camping_rental.server.domain.exception.dto;

// HTTP STATUS 400
public class NotMatchedFormatException extends RuntimeException{
    public NotMatchedFormatException() {
        super();
    }
    public NotMatchedFormatException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotMatchedFormatException(String message) {
        super(message);
    }
    public NotMatchedFormatException(Throwable cause) {
        super(cause);
    }
}
