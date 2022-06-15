package com.camping_rental.server.domain.user.service;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.user.dto.UserDto;
import com.camping_rental.server.domain.user.entity.UserEntity;
import com.camping_rental.server.domain.user.enums.UserAllowedAccessCountEnum;
import com.camping_rental.server.domain.user.enums.UserLoginTypeEnum;
import com.camping_rental.server.domain.user.enums.UserRoleEnum;
import com.camping_rental.server.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import javax.persistence.Column;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserBusinessService {
    @Value("${app.jwt.phone-validation.secret}")
    private String JWT_PHONE_VALIDATION_SECRET;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public void checkDuplicateUsername(String username) {
        UserEntity userEntity = userService.searchByUsername(username);

        if (userEntity != null) {
            throw new NotMatchedFormatException("이미 사용중인 아이디 입니다.");
        }
    }

    /*
    username 형식 체크
    username 중복 체크
    password 형식 체크
    password, passwordChecker 동일성 체크
    phoneNumber 형식 체크
    phoneNumber 중복 체크
    phoneNumber, phoneNumberValidcationCode, token valify 체크
    UserEntity setting
    DB save
    cp_phone_validation_token cookie 삭제
     */
    @Transactional
    public void signup(HttpServletRequest request, HttpServletResponse response, UserDto.LocalSignup userSignupDto) {
        String USERNAME = userSignupDto.getUsername();
        String PASSWORD = userSignupDto.getPassword();
        String PASSWORD_CHECKER = userSignupDto.getPasswordChecker();
        String PHONE_NUMBER = userSignupDto.getPhoneNumber();
        String PHONE_NUMBER_VALIDATION_CODE = userSignupDto.getPhoneNumberValidationCode();

        /*
        username 형식 체크
         */
        if (!DataFormatUtils.isPassSignupUsername(USERNAME)) {
            throw new NotMatchedFormatException("입력하신 아이디 형식이 정확한지 확인하여 주세요.");
        }

        /*
        username 중복 체크
         */
        if (userService.searchByUsername(USERNAME) != null) {
            throw new NotMatchedFormatException("이미 사용중인 아이디 입니다.");
        }

        /*
        password 형식 체크
         */
        if (!DataFormatUtils.isPassSignupPassword(PASSWORD)) {
            throw new NotMatchedFormatException("입력하신 패스워드 형식이 정확한지 확인하여 주세요.");
        }

        /*
        password, passwordChecker 동일성 체크
         */
        if (!PASSWORD.equals(PASSWORD_CHECKER)) {
            throw new NotMatchedFormatException("입력하신 패스워드를 다시 확인하여 주세요.");
        }

        /*
        phoneNumber 형식 체크
         */
        if (!DataFormatUtils.isPassSignupPhoneNumber(PHONE_NUMBER)) {
            throw new NotMatchedFormatException("입력하신 휴대전화 형식이 정확한지 확인하여 주세요.");
        }

        /*
        phoneNumber 중복 체크
         */
        if (userService.searchByPhoneNumberAndLoginType(PHONE_NUMBER, UserLoginTypeEnum.LOCAL.getValue()) != null) {
            throw new NotMatchedFormatException("이미 해당 휴대전화로 가입된 아이디가 있습니다.");
        }

        Cookie phoneValidationCookie = WebUtils.getCookie(request, CustomCookieUtils.COOKIE_NAME_PHONE_VALIDATION_TOKEN);

        String phoneValidationToken = null;
        if (phoneValidationCookie == null) {
            throw new NotMatchedFormatException("인증번호가 정확하지 않습니다.");
        }

        phoneValidationToken = phoneValidationCookie.getValue();

        String jwtSecret = PHONE_NUMBER + PHONE_NUMBER_VALIDATION_CODE + JWT_PHONE_VALIDATION_SECRET;
        CustomJwtUtils.parseJwt(jwtSecret, phoneValidationToken, "인증번호가 정확하지 않습니다.");

        /*
        UserEntity setting
         */
        UUID userId = UUID.randomUUID();
        String salt = UUID.randomUUID().toString();
        String encPassword = passwordEncoder.encode(userSignupDto.getPassword() + salt);

        UserEntity userEntity = UserEntity.builder()
                .cid(null)
                .id(userId)
                .loginType(UserLoginTypeEnum.LOCAL.getValue())
                .socialPlatform(null)
                .socialPlatformId(null)
                .username(userSignupDto.getUsername())
                .password(encPassword)
                .salt(salt)
                .email(null)
                .name(null)
                .nickname(null)
                .phoneNumber(userSignupDto.getPhoneNumber())
                .roles(UserRoleEnum.USER.getValue())
                .allowedAccessCount(UserAllowedAccessCountEnum.DEFUALT.getValue())
                .updatedAt(CustomDateUtils.getCurrentDateTime())
                .createdAt(CustomDateUtils.getCurrentDateTime())
                .build();

        /*
        DB save
         */
        userService.saveAndModify(userEntity);

        /*
        cp_phone_validation_token cookie 삭제
         */
        ResponseCookie phoneValidationTokenCookie = ResponseCookie.from(CustomCookieUtils.COOKIE_NAME_PHONE_VALIDATION_TOKEN, null)
                .domain(CustomCookieUtils.COOKIE_DOMAIN)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, phoneValidationTokenCookie.toString());
    }
}
