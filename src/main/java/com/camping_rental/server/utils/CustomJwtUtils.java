package com.camping_rental.server.utils;

public class CustomJwtUtils {
    public final static Integer JWT_TOKEN_EXPIRATION = 10 * 60 * 1000;  // milliseconds - 10분

    public final static Integer REFRESH_TOKEN_JWT_EXPIRATION = 5 * 24 * 60 * 60 * 1000;   // milliseconds - 5일/**/

    public final static Integer CSRF_TOKEN_JWT_EXPIRATION = 5 * 1000;  // milliseconds - 5000ms -> 5s

    public final static Integer PHONE_VALIDATION_TOKEN_JWT_EXPIRATION = 30 * 60 * 1000;   // milliseconds - 30분
    public final static Integer PHONE_VALIDATION_VF_TOKEN_JWT_EXPIRATION = 30 * 60 * 1000;   // milliseconds - 30분

    public final static Integer EMAIL_VALIDATION_TOKEN_JWT_EXPIRATION = 3 * 60 * 1000;   // milliseconds - 3분
    public final static Integer EMAIL_VALIDATION_VF_TOKEN_JWT_EXPIRATION = 10 * 60 * 1000;   // milliseconds - 10분
}
