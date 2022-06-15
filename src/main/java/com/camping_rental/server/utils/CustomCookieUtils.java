package com.camping_rental.server.utils;

import org.springframework.beans.factory.annotation.Value;

public interface CustomCookieInterface {
    @Value()

    final static String COOKIE_DOMAIN = "localhost"; // PROD : viewday.co.kr | DEV : localhost

    final static Integer JWT_TOKEN_COOKIE_EXPIRATION = 5 * 24 * 60 * 60; // seconds - 5일
    final static Integer CSRF_TOKEN_COOKIE_EXPIRATION = 5; // seconds - 5s

    final static Integer PHONE_VALIDATION_COOKIE_EXPIRATION = 30 * 60; // seconds - 30분
    final static Integer PHONE_VALIDATION_VF_COOKIE_EXPIRATION = 30 * 60; // seconds - 30분

    final static Integer EMAIL_VALIDATION_COOKIE_EXPIRATION = 5 * 60; // seconds - 5분
    final static Integer EMAIL_VALIDATION_VF_COOKIE_EXPIRATION = 10 * 60; // seconds - 10분

    final static boolean SECURE = false; // PROD : true | DEV : false
}
