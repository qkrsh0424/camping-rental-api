package com.camping_rental.server.config.csrf;

import com.camping_rental.server.config.exception.CsrfAccessDeniedException;
import com.camping_rental.server.config.exception.CsrfExpiredJwtException;
import com.camping_rental.server.config.exception.CsrfNullPointerException;
import com.camping_rental.server.utils.CsrfTokenUtils;
import com.camping_rental.server.utils.CustomCookieUtils;
import com.camping_rental.server.utils.CustomJwtUtils;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 수정
 * 1. csrf 쿠키를 두개를 넘겨준다. (1) => csrf_jwt_token : csrf_token 값을 가진 JWT, (2) => csrf_token : UUID
 */
@Slf4j
public class CsrfAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String CSRF_TOKEN_SECRET = CsrfTokenUtils.getCsrfTokenSecret();

        // GET 메소드는 CsrfFilter를 타지 않는다
        if (request.getMethod().equals("GET")) {
            chain.doFilter(request, response);
            return;
        } else {
            try {
                Cookie csrfTokenCookie = WebUtils.getCookie(request, CustomCookieUtils.COOKIE_NAME_API_CSRF_TOKEN);

                String csrfJwtToken = csrfTokenCookie.getValue();

                String xCsrfToken = request.getHeader("X-XSRF-TOKEN");
                String secret = xCsrfToken + CSRF_TOKEN_SECRET;

                Claims claims = CustomJwtUtils.parseJwt(secret, csrfJwtToken);

                chain.doFilter(request, response);
                return;
            } catch (ExpiredJwtException e) {     // 토큰 만료
                throw new CsrfExpiredJwtException("Csrf jwt expired.");
            } catch (NullPointerException e) {
                throw new CsrfNullPointerException("Csrf cookie does not exist.");
            } catch (IllegalArgumentException e) {
                throw new CsrfNullPointerException("Csrf jwt does not exist.");
            } catch (UnsupportedJwtException e) {
                throw new CsrfAccessDeniedException("ClaimsJws argument does not represent an Claims JWS");
            } catch (MalformedJwtException e) {
                throw new CsrfAccessDeniedException("ClaimsJws string is not a valid JWS. ");
            } catch (SignatureException e) {
                throw new CsrfAccessDeniedException("ClaimsJws JWS signature validation fails");
            }
        }
    }
}
