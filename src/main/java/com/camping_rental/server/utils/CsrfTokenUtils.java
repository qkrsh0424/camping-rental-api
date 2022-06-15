package com.camping_rental.server.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CsrfTokenUtils {
    private static String CSRF_TOKEN_SECRET;

    @Value("${app.jwt.csrf-token.secret}")
    public void setCsrfJwtSecret(String csrfJwtSecret) {
        CsrfTokenUtils.CSRF_TOKEN_SECRET = csrfJwtSecret;
    }

    public static String generateCsrfJwtToken(String xCsrfToken) {
        String secret = xCsrfToken + CSRF_TOKEN_SECRET;

        return CustomJwtUtils.generateJwtToken(
                "CSRF_JWT",
                CustomJwtUtils.CSRF_TOKEN_JWT_EXPIRATION,
                secret
        );
    }

    public static String getCsrfTokenSecret(){
        return CSRF_TOKEN_SECRET;
    }
}
