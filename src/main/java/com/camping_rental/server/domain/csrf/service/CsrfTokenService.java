package com.camping_rental.server.domain.csrf.service;

import com.camping_rental.server.utils.CsrfTokenUtils;
import com.camping_rental.server.utils.CustomCookieUtils;
import com.camping_rental.server.utils.CustomJwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
public class CsrfTokenService {
    public void getCsrfToken(HttpServletResponse response) {

        // 토큰 생성 및 쿠키 설정
        String csrfTokenId = UUID.randomUUID().toString();
        String csrfJwtToken = CsrfTokenUtils.generateCsrfJwtToken(csrfTokenId);

        ResponseCookie csrfJwt = ResponseCookie.from(CustomCookieUtils.COOKIE_NAME_API_CSRF_TOKEN, csrfJwtToken)
                .httpOnly(true)
                .domain(CustomCookieUtils.COOKIE_DOMAIN)
                .secure(CustomCookieUtils.SECURE)
                .sameSite("Strict")
                .path("/")
                .maxAge(CustomCookieUtils.CSRF_TOKEN_COOKIE_EXPIRATION)
                .build();

        ResponseCookie csrfToken = ResponseCookie.from(CustomCookieUtils.COOKIE_NAME_X_API_CSRF_TOKEN, csrfTokenId)
                .secure(CustomCookieUtils.SECURE)
                .domain(CustomCookieUtils.COOKIE_DOMAIN)
                .sameSite("Strict")
                .path("/")
                .maxAge(CustomCookieUtils.CSRF_TOKEN_COOKIE_EXPIRATION)
                .build();


        response.addHeader(HttpHeaders.SET_COOKIE, csrfJwt.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, csrfToken.toString());
    }
}
