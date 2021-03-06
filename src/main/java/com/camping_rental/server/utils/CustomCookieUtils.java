package com.camping_rental.server.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomCookieUtils {
    public static String COOKIE_DOMAIN;
    public static boolean SECURE = false; // PROD : true | DEV : false

    public final static Integer CSRF_TOKEN_COOKIE_EXPIRATION = 5; // seconds - 5s

    public final static Integer PHONE_VALIDATION_COOKIE_EXPIRATION = 30 * 60; // seconds - 30분
    public final static Integer EMAIL_VALIDATION_COOKIE_EXPIRATION = 30 * 60; // seconds - 30분
    public final static Integer ACCESS_TOKEN_COOKIE_EXPIRATION = 5 * 24 * 60 * 60; // seconds - 5일

    public final static String COOKIE_NAME_ACCESS_TOKEN = "cp_ac_token";
    public final static String COOKIE_NAME_PHONE_VALIDATION_TOKEN = "cp_phone_validation_token";
    public final static String COOKIE_NAME_EMAIL_VALIDATION_TOKEN = "cp_email_validation_token";
    public final static String COOKIE_NAME_API_CSRF_TOKEN = "api_csrf_token";
    public final static String COOKIE_NAME_X_API_CSRF_TOKEN = "x_api_csrf_token";


    @Value("${app.cookie.domain}")
    public void setCookieDomain(String domain) {
        CustomCookieUtils.COOKIE_DOMAIN = domain;
    }

    @Value("${app.cookie.secure}")
    public void setCookieSecure(boolean secure) {
        CustomCookieUtils.SECURE = secure;
    }
}
