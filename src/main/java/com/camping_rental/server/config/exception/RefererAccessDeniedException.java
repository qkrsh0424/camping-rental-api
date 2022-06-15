package com.camping_rental.server.config.exception;

public class RefererAccessDeniedException extends RuntimeException{
    public RefererAccessDeniedException(String msg) {
        super(msg);
    }
    public RefererAccessDeniedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
