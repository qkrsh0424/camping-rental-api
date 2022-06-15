package com.camping_rental.server.utils;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ValidationJwtTokenUtils {

    private static String JWT_PHONE_VALIDATION_SECRET;
    private static String JWT_EMAIL_VALIDATION_SECRET;

    @Value("${app.jwt.phone-validation.secret}")
    public void setPhoneAuthJwtSecret(String phoneAuthJwtSecret) {
        ValidationJwtTokenUtils.JWT_PHONE_VALIDATION_SECRET = phoneAuthJwtSecret;
    }

    @Value("${app.jwt.email-validation.secret}")
    public void setEmailAuthJwtSecret(String emailAuthJwtSecret) {
        ValidationJwtTokenUtils.JWT_EMAIL_VALIDATION_SECRET = emailAuthJwtSecret;
    }

    public static String getPhoneValidationNumberJwtToken(String authNumber, String phoneNumber) {
        String PHONE_AUTH_JWT_KEY = phoneNumber + authNumber + JWT_PHONE_VALIDATION_SECRET;

        JwtBuilder builder = Jwts.builder()
                .setSubject("PHONE_VALIDATION_JWT")
                .setHeader(createHeader())
                .setExpiration(createTokenExpiration(CustomJwtUtils.PHONE_VALIDATION_TOKEN_JWT_EXPIRATION))
                .signWith(createSigningKey(PHONE_AUTH_JWT_KEY), SignatureAlgorithm.HS256);

        return builder.compact();
    }

    public static String getPhoneAuthVerifiedJwtToken(String phoneNumber) {
        String PHONE_AUTH_JWT_KEY = phoneNumber + JWT_PHONE_VALIDATION_SECRET;

        JwtBuilder builder = Jwts.builder()
                .setSubject("PHONE_AUTH_VF_JWT")
                .setHeader(createHeader())
                .setExpiration(createTokenExpiration(CustomJwtUtils.PHONE_VALIDATION_VF_TOKEN_JWT_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, createSigningKey(PHONE_AUTH_JWT_KEY));

        return builder.compact();
    }

//    public static String getEmailAuthNumberJwtToken(String authNumber, String email) {
//        String EMAIL_AUTH_JWT_KEY = authNumber + JWT_EMAIL_VALIDATION_SECRET;
//
//        JwtBuilder builder = Jwts.builder()
//                .setSubject("PHONE_AUTH_JWT")
//                .setHeader(createHeader())
//                .setClaims(createEmailAuthClaims(email))
//                .setExpiration(createTokenExpiration(CustomJwtInterface.EMAIL_VALIDATION_TOKEN_JWT_EXPIRATION))
//                .signWith(SignatureAlgorithm.HS256, createSigningKey(EMAIL_AUTH_JWT_KEY));
//
//        return builder.compact();
//    }
//
//    public static String getEmailAuthVerifiedJwtToken(String email) {
//        String EMAIL_AUTH_JWT_KEY = email + JWT_EMAIL_VALIDATION_SECRET;
//
//        JwtBuilder builder = Jwts.builder()
//                .setSubject("EMAIL_AUTH_VF_JWT")
//                .setHeader(createHeader())
//                .setExpiration(createTokenExpiration(CustomJwtInterface.EMAIL_VALIDATION_VF_TOKEN_JWT_EXPIRATION))
//                .signWith(SignatureAlgorithm.HS256, createSigningKey(EMAIL_AUTH_JWT_KEY));
//
//        return builder.compact();
//    }

    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    // JWT Palyod
    private static Map<String, Object> createPhoneAuthClaims(String phoneNumber) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("phoneNumber", phoneNumber);
        return claims;
    }

    private static Map<String, Object> createEmailAuthClaims(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        return claims;
    }

    private static Date createTokenExpiration(Integer expirationTime) {
        Date expiration = new Date(System.currentTimeMillis() + expirationTime);
        return expiration;
    }

    private static Key createSigningKey(String tokenSecret) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(tokenSecret);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public static Key generateSigningKey(String secret) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
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
            throw new NotMatchedFormatException("인증 요청시간이 만료되었습니다.");
        } catch (NullPointerException e) {   // Phone Auth Number 쿠키가 존재하지 않는다면
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

    public static boolean isValidToken(Cookie phoneAuthNumCookie) {

        String phoneAuthToken = phoneAuthNumCookie.getValue();

        try {
            Claims claims = Jwts.parser().setSigningKey(JWT_PHONE_VALIDATION_SECRET).parseClaimsJws(phoneAuthToken).getBody();
            log.info("expireTime :" + claims.getExpiration());
            return true;
        } catch (ExpiredJwtException exception) {
            log.error("Token Expired");
            return false;
        } catch (JwtException exception) {
            log.error("Token Tampered");
            return false;
        } catch (NullPointerException exception) {
            log.error("Token is null");
            return false;
        }
    }
}