package com.camping_rental.server.config.auth;

import com.camping_rental.server.domain.refresh_token.entity.RefreshTokenEntity;
import com.camping_rental.server.domain.refresh_token.repository.RefreshTokenRepository;
import com.camping_rental.server.domain.user.entity.UserEntity;
import com.camping_rental.server.utils.AuthTokenUtils;
import com.camping_rental.server.utils.CustomCookieUtils;
import com.camping_rental.server.utils.CustomJwtUtils;
import io.jsonwebtoken.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private RefreshTokenRepository refreshTokenRepository;

    public JwtAuthorizationFilter(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Cookie jwtTokenCookie = WebUtils.getCookie(request, CustomCookieUtils.COOKIE_NAME_ACCESS_TOKEN);

        // 액세스 토큰 쿠키가 있는지 확인, 만약에 없다면 체인을 계속 타게하고 있다면 검증한다.
        if (jwtTokenCookie == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtTokenCookie.getValue();
        Claims claims = null;

        try {
            claims = CustomJwtUtils.parseJwt(AuthTokenUtils.getAccessTokenSecret(), accessToken);
        } catch (ExpiredJwtException e) {
            filterChain.doFilter(request, response);
            return;
        } catch (UnsupportedJwtException e) {
            filterChain.doFilter(request, response);
            return;
        } catch (MalformedJwtException e) {
            filterChain.doFilter(request, response);
            return;
        } catch (SignatureException e) {
            filterChain.doFilter(request, response);
            return;
        } catch (IllegalArgumentException e) {
            filterChain.doFilter(request, response);
            return;
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        if (claims == null) {
            filterChain.doFilter(request, response);
            return;
        }

        UUID id = UUID.fromString(claims.get("id").toString());
        String username = claims.get("username").toString();

        UserEntity userEntity = UserEntity.builder()
                .id(id)
                .username(username)
                .build();

        this.saveAuthenticationToSecurityContextHolder(userEntity);

        filterChain.doFilter(request, response);
    }

    private void saveAuthenticationToSecurityContextHolder(UserEntity userEntity) {
        PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

        // Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
        // Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, null);

        // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
