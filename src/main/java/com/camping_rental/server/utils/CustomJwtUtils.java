package com.camping_rental.server.utils;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomJwtUtils {
    public static String JWT_ISSUER;

    public final static Integer ACCESS_TOKEN_JWT_EXPIRATION = 10 * 60 * 1000;  // milliseconds - 10분
    public final static Integer REFRESH_TOKEN_JWT_EXPIRATION = 5 * 24 * 60 * 60 * 1000;   // milliseconds - 5일/**/
    public final static Integer CSRF_TOKEN_JWT_EXPIRATION = 5 * 1000;  // milliseconds - 5000ms -> 5s
    public final static Integer PHONE_VALIDATION_TOKEN_JWT_EXPIRATION = 30 * 60 * 1000;   // milliseconds - 30분
    public final static Integer EMAIL_VALIDATION_TOKEN_JWT_EXPIRATION = 30 * 60 * 1000;   // milliseconds - 30분

    @Value("${app.jwt.issuer}")
    public void setJwtIssuer(String issuer) {
        CustomJwtUtils.JWT_ISSUER = issuer;
    }

    /**
     * <p>Jwt 토큰 생성 메서드</p>
     *
     * @param subject        - String value
     * @param expirationTime - time for milliseceonds
     * @param secret         - String value
     * @return
     */
    public static String generateJwtToken(String subject, Integer expirationTime, String secret) {
        String jwt = Jwts.builder()
                .setSubject(subject)
                .setHeader(createHeader())
                .setIssuer(JWT_ISSUER)
                .setIssuedAt(createTokenIssuedAt())
                .setExpiration(createTokenExpiration(expirationTime))
                .signWith(generateSigningKey(secret), SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }

    public static String generateJwtToken(String subject, Map<String, Object> claims, Integer expirationTime, String secret) {
        String jwt = Jwts.builder()
                .setSubject(subject)
                .setHeader(createHeader())
                .setClaims(claims)
                .setIssuer(JWT_ISSUER)
                .setIssuedAt(createTokenIssuedAt())
                .setExpiration(createTokenExpiration(expirationTime))
                .signWith(generateSigningKey(secret), SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }

    public static String generateJwtToken(String subject, Map<String, Object> header, Map<String, Object> claims, Integer expirationTime, String secret) {
        String jwt = Jwts.builder()
                .setSubject(subject)
                .setHeader(header)
                .setClaims(claims)
                .setIssuer(JWT_ISSUER)
                .setIssuedAt(createTokenIssuedAt())
                .setExpiration(createTokenExpiration(expirationTime))
                .signWith(generateSigningKey(secret), SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }

    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    private static Date createTokenIssuedAt() {
        Date expiration = new Date(System.currentTimeMillis());
        return expiration;
    }

    private static Date createTokenExpiration(Integer expirationTime) {
        Date expiration = new Date(System.currentTimeMillis() + expirationTime);
        return expiration;
    }

    public static Key generateSigningKey(String secret) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * Jwt 토큰 파싱 매서드
     *
     * @param secret - String value
     * @param jwt
     * @return
     * @throws ExpiredJwtException
     * @throws NullPointerException
     * @throws IllegalArgumentException
     * @throws UnsupportedJwtException
     * @throws MalformedJwtException
     * @throws SignatureException
     */
    public static Claims parseJwt(String secret, String jwt) throws ExpiredJwtException, NullPointerException, IllegalArgumentException, UnsupportedJwtException, MalformedJwtException, SignatureException {
        Key signingKey = generateSigningKey(secret);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }

    public static Claims parseJwt(String secret, String jwt, String exceptionMemo) {
        Key signingKey = generateSigningKey(secret);
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (ExpiredJwtException e) {     // 토큰 만료
            throw new NotMatchedFormatException("인증기간이 만료되었습니다.");
        } catch (NullPointerException e) {
            throw new NotMatchedFormatException(exceptionMemo);
        } catch (IllegalArgumentException e) {
            throw new NotMatchedFormatException(exceptionMemo);
        } catch (UnsupportedJwtException e) {
            throw new NotMatchedFormatException(exceptionMemo);
        } catch (MalformedJwtException e) {
            throw new NotMatchedFormatException(exceptionMemo);
        } catch (SignatureException e) {
            throw new NotMatchedFormatException(exceptionMemo);
        }

        return claims;
    }
}
