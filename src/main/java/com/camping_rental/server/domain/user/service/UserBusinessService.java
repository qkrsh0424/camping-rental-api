package com.camping_rental.server.domain.user.service;

import com.camping_rental.server.domain.enums.DeletedFlagEnums;
import com.camping_rental.server.domain.exception.dto.InvalidUserException;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.refresh_token.entity.RefreshTokenEntity;
import com.camping_rental.server.domain.refresh_token.service.RefreshTokenService;
import com.camping_rental.server.domain.room.entity.RoomEntity;
import com.camping_rental.server.domain.room.service.RoomService;
import com.camping_rental.server.domain.user.dto.UserDto;
import com.camping_rental.server.domain.user.entity.UserEntity;
import com.camping_rental.server.domain.user.enums.UserAllowedAccessCountEnum;
import com.camping_rental.server.domain.user.enums.UserLoginTypeEnum;
import com.camping_rental.server.domain.user.enums.UserRoleEnum;
import com.camping_rental.server.domain.user.vo.UserVo;
import com.camping_rental.server.domain.user_consent.entity.UserConsentEntity;
import com.camping_rental.server.domain.user_consent.service.UserConsentService;
import com.camping_rental.server.utils.*;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserBusinessService {
    private final PasswordEncoder passwordEncoder;

    private final UserService userService;
    private final UserConsentService userConsentService;
    private final RefreshTokenService refreshTokenService;
    private final RoomService roomService;

    @Transactional(readOnly = true)
    public String checkDuplicateUsername(String username) {
        UserEntity userEntity = userService.searchByUsernameOrNull(username);

        if (userEntity != null) {
//            throw new NotMatchedFormatException("이미 사용중인 아이디 입니다.");
            return "duplicated";
        }

        return "not_duplicated";
    }

    @Transactional(readOnly = true)
    public String checkDuplicateUsernameAndPhoneNumber(String username, String phoneNumber) {
        UserEntity userEntity = userService.searchByUsernameAndPhoneNumberAndLoginType(username, phoneNumber, UserLoginTypeEnum.LOCAL.getValue());

        if (userEntity != null) {
            return "duplicated";
        }

        return "not_duplicated";
    }

    /*
    액세스 토큰 쿠키값 유무 확인, 없다면 401 에러
    액세스 토큰의 정상 유무 판단, ExpirationJwtException 에러가 난다면 재발급 가능한 토큰, 이 외의 에러가 난다면 재발급 불가능 토큰으로 401 에러
    액세스 토큰의 claims 에서 refreshTokenId 값을 추출
    refreshTokenId 값으로 RefreshTokenEntity 조회
    RefreshTokenEntity 가 Null 이라면 401 에러
    RefreshTokenEntity 가 가지고 있는 jwt 값을 파싱한다. 파싱 에러가 난다면 401 에러
    refreshTokenJwt 의 유저 id 와 username 값으로 accessTokenJwt를 만들어준다.
    새로운 액세스 토큰을 쿠키에 저장해 준다.
     */
    @Transactional(readOnly = true)
    public void reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie jwtTokenCookie = WebUtils.getCookie(request, CustomCookieUtils.COOKIE_NAME_ACCESS_TOKEN);

        /*
        액세스 토큰 쿠키값 유무 확인, 없다면 401 에러
         */
        if (jwtTokenCookie == null) {
            throw new InvalidUserException("접근 토큰이 만료 되었습니다. 재로그인 해주시기 바랍니다.");
        }

        String accessToken = jwtTokenCookie.getValue();
        Claims claims = null;

        /*
        액세스 토큰의 정상 유무 판단, ExpirationJwtException 에러가 난다면 재발급 가능한 토큰, 이 외의 에러가 난다면 재발급 불가능 토큰으로 401 에러
         */
        try {
            claims = CustomJwtUtils.parseJwt(AuthTokenUtils.getAccessTokenSecret(), accessToken);
        } catch (ExpiredJwtException e) {
            // 만료된 액세스토큰 claims를 이용해 리프레시 토큰 id를 추출할 수 있다.
            claims = e.getClaims();
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new InvalidUserException("리소스에 접근이 불가능한 토큰 입니다.");
        } catch (Exception e) {
            throw new InvalidUserException("리소스에 접근이 불가능한 토큰 입니다.");
        }

        UUID refreshTokenId = null;

        /*
        액세스 토큰의 claims 에서 refreshTokenId 값을 추출
         */
        try {
            refreshTokenId = UUID.fromString(claims.get("refreshTokenId").toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidUserException("리소스에 접근이 불가능한 토큰 입니다.");
        }

        /*
        refreshTokenId 값으로 RefreshTokenEntity 조회
         */
        RefreshTokenEntity refreshTokenEntity = refreshTokenService.searchById(refreshTokenId);

        /*
        RefreshTokenEntity 가 Null 이라면 401 에러
         */
        if (refreshTokenEntity == null) {
            this.logout(request, response);
            throw new InvalidUserException("로그인이 만료 되었습니다.");
        }

        /*
        RefreshTokenEntity 가 가지고 있는 jwt 값을 파싱한다. 파싱 에러가 난다면 401 에러
         */
        Claims refreshTokenClaims = null;
        try {
            refreshTokenClaims = CustomJwtUtils.parseJwt(AuthTokenUtils.getRefreshTokenSecret(), refreshTokenEntity.getRefreshToken());
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            this.logout(request, response);
            throw new InvalidUserException("로그인이 만료 되었습니다.");
        } catch (Exception e) {
            this.logout(request, response);
            throw new InvalidUserException("로그인이 만료 되었습니다.");
        }

        /*
        refreshTokenJwt 의 유저 id 와 username 값으로 새로운 accessTokenJwt를 만들어준다.
         */
        UUID id = UUID.fromString(refreshTokenClaims.get("id").toString());
        String username = refreshTokenClaims.get("username").toString();

        UserEntity user = UserEntity.builder()
                .id(id)
                .username(username)
                .build();

        String newAccessToken = AuthTokenUtils.generateAccessTokenJwt(user, refreshTokenEntity.getId());

        /*
        새로운 액세스 토큰을 쿠키에 저장해 준다.
         */
        ResponseCookie tokenCookie = ResponseCookie.from(CustomCookieUtils.COOKIE_NAME_ACCESS_TOKEN, newAccessToken)
                .httpOnly(true)
                .secure(CustomCookieUtils.SECURE)
                .sameSite("Strict")
                .domain(CustomCookieUtils.COOKIE_DOMAIN)
                .path("/")
                .maxAge(CustomCookieUtils.ACCESS_TOKEN_COOKIE_EXPIRATION)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
    }

    @Transactional
    public void signup(HttpServletRequest request, HttpServletResponse response, UserDto.LocalSignup userSignupDto) {
        String USERNAME = userSignupDto.getUsername();
        String NICKNAME = userSignupDto.getNickname();
        String PHONE_NUMBER = userSignupDto.getPhoneNumber();
        String PASSWORD = userSignupDto.getPassword();

        UserDto.LocalSignup.checkFormValid(request, userSignupDto, userService);

        /*
        UserEntity setting
         */
        UUID userId = UUID.randomUUID();
        String salt = UUID.randomUUID().toString();
        String encPassword = passwordEncoder.encode(PASSWORD + salt);
        UUID userConsentId = UUID.randomUUID();

        UserEntity userEntity = UserEntity.builder()
                .cid(null)
                .id(userId)
                .loginType(UserLoginTypeEnum.LOCAL.getValue())
                .socialPlatform(null)
                .socialPlatformId(null)
                .username(USERNAME)
                .password(encPassword)
                .salt(salt)
                .email(null)
                .name(null)
                .nickname(NICKNAME)
                .phoneNumber(PHONE_NUMBER)
                .profileImageUri("")
                .roles(UserRoleEnum.USER.getValue())
                .allowedAccessCount(UserAllowedAccessCountEnum.DEFUALT.getValue())
                .updatedAt(CustomDateUtils.getCurrentDateTime())
                .createdAt(CustomDateUtils.getCurrentDateTime())
                .deletedFlag(DeletedFlagEnums.EXIST.getValue())
                .roomId(null)
                .build();

        UserConsentEntity userConsentEntity = UserConsentEntity.builder()
                .cid(null)
                .id(userConsentId)
                .serviceTermsYn(userSignupDto.getServiceTermsYn())
                .privacyPolicyYn(userSignupDto.getPrivacyPolicyYn())
                .marketingYn(userSignupDto.getMarketingYn())
                .marketingPhoneYn(userSignupDto.getMarketingYn().equals("y") ? "y" : "n")
                .marketingEmailYn("n")
                .deletedFlag(DeletedFlagEnums.EXIST.getValue())
                .userId(userId)
                .build();
        /*
        DB save
         */
        userService.saveAndGet(userEntity);
        userConsentService.saveAndModify(userConsentEntity);

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

    /*
    username, login_type='local' 로 userEntity 조회
    user의 존재 유무 확인 없다면 401 에러
    UserEntity 의 salt 와 입력받은 password 로 fullPassword 를 구성, UserEntity 의 password와 일치성 비교 후 일치하지 않다면 401 에러
    accessToken, refreshToken 생성
    refreshTokenEntity 구성
    신규 refreshTokenEntity DB 저장 후, AllowedAccessCount 의 값에 따라서 oldRefreshToken을 제거해준다.
    accessToken을 쿠키에 등록해준다.
     */
    @Transactional
    public void login(HttpServletRequest request, HttpServletResponse response, UserDto.LocalLogin userLoginDto) {
        String username = userLoginDto.getUsername();
        String password = userLoginDto.getPassword();

        /*
        username, login_type='local' 로 userEntity 조회
        */
        UserEntity userEntity = userService.searchByUsernameAndLoginType(username, UserLoginTypeEnum.LOCAL.getValue());

        /*
        user의 존재 유무 확인 없다면 401 에러
         */
        if (userEntity == null) {
            throw new InvalidUserException("입력한 아이디 및 패스워드를 확인해 주세요.");
        }

        /*
        UserEntity 의 salt 와 입력받은 password 로 fullPassword 를 구성, UserEntity 의 password와 일치성 비교 후 일치하지 않다면 401 에러
         */
        UUID USER_ID = userEntity.getId();
        String ENC_PASSWORD = userEntity.getPassword();
        String SALT = userEntity.getSalt();
        String FULL_PASSWORD = password + SALT;
        Integer ALLOWED_ACCESS_COUNT = userEntity.getAllowedAccessCount();
        if (!passwordEncoder.matches(FULL_PASSWORD, ENC_PASSWORD)) {
            throw new InvalidUserException("입력한 아이디 및 패스워드를 확인해 주세요.");
        }

        /*
        acccess token, refresh token 생성
         */
        UUID REFRESH_TOKEN_ID = UUID.randomUUID();

        String refreshToken = AuthTokenUtils.generateRefreshTokenJwt(userEntity);
        String accessToken = AuthTokenUtils.generateAccessTokenJwt(userEntity, REFRESH_TOKEN_ID);

        /*
        refreshTokenEntity 구성
         */
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .cid(null)
                .id(REFRESH_TOKEN_ID)
                .userId(USER_ID)
                .refreshToken(refreshToken)
                .createdAt(CustomDateUtils.getCurrentDateTime())
                .updatedAt(CustomDateUtils.getCurrentDateTime())
                .deletedFlag(DeletedFlagEnums.EXIST.getValue())
                .build();

        /*
        신규 refreshTokenEntity DB 저장 후, AllowedAccessCount 의 값에 따라서 oldRefreshToken을 제거해준다.
         */
        refreshTokenService.saveAndModify(refreshTokenEntity);
        refreshTokenService.deleteOldRefreshToken(USER_ID, ALLOWED_ACCESS_COUNT);

        ResponseCookie tokenCookie = ResponseCookie.from(CustomCookieUtils.COOKIE_NAME_ACCESS_TOKEN, accessToken)
                .httpOnly(true)
                .domain(CustomCookieUtils.COOKIE_DOMAIN)
                .secure(CustomCookieUtils.SECURE)
                .sameSite("Strict")
                .path("/")
                .maxAge(CustomCookieUtils.ACCESS_TOKEN_COOKIE_EXPIRATION)
                .build();

        /*
        accessToken을 쿠키에 등록해준다.
         */
        response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());

    }

    @Transactional(readOnly = true)
    public Object searchUserInfo(HttpServletRequest request) {
        Cookie jwtTokenCookie = WebUtils.getCookie(request, CustomCookieUtils.COOKIE_NAME_ACCESS_TOKEN);
        if (jwtTokenCookie == null) {
            return null;
        }

        UUID userId = userService.getUserIdOrThrow();

        if (userId == null) {
            return null;
        }

        UserEntity userEntity = userService.searchById(userId);

        if (userEntity == null) {
            return null;
        }

        UserVo.Basic userDto = UserVo.Basic.toVo(userEntity);

        return userDto;
    }

    @Transactional(readOnly = true)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        ResponseCookie tokenCookie = ResponseCookie.from(CustomCookieUtils.COOKIE_NAME_ACCESS_TOKEN, null)
                .httpOnly(true)
                .domain(CustomCookieUtils.COOKIE_DOMAIN)
                .secure(CustomCookieUtils.SECURE)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
    }

    @Transactional(readOnly = true)
    public Object findUsername(HttpServletRequest request, HttpServletResponse response, UserDto.FindUsername userDto) {
        UserDto.FindUsername.checkFormValid(request, userDto);

        UserEntity userEntity = userService.searchByPhoneNumberAndLoginType(userDto.getPhoneNumber(), UserLoginTypeEnum.LOCAL.getValue());

        String username = userEntity.getUsername();
        String encUsername = UserDto.FindUsername.returnEncUsername(username);

        UserVo.FindUsername userVo = UserVo.FindUsername.builder()
                .username(encUsername)
                .createdAt(userEntity.getCreatedAt())
                .build();

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
        return userVo;
    }

    @Transactional
    public void changeNickname(String nickname) {
        UUID userId = userService.getUserIdOrThrow();

        UserEntity userEntity = userService.searchById(userId);

        if (!DataFormatUtils.isPassNicknameFormatValid(nickname)) {
            throw new NotMatchedFormatException("2-15자로 지정해 주세요.");
        }

        if (userEntity.getNickname().equals(nickname)) {
            return;
        }

        userEntity.setNickname(nickname);
        userEntity.setUpdatedAt(CustomDateUtils.getCurrentDateTime());
    }

    @Transactional
    public void changePhoneNumber(HttpServletRequest request, HttpServletResponse response, String phoneNumber, String phoneNumberValidationCode) {
        UUID userId = userService.getUserIdOrThrow();

        /*
        휴대전화 형식 체크
         */
        if (!DataFormatUtils.isPassPhoneNumberFormatValid(phoneNumber)) {
            throw new NotMatchedFormatException("입력하신 휴대전화 형식이 정확한지 확인하여 주세요.");
        }

        /*
        휴대전화 인증번호 체크
         */
        Cookie phoneValidationCookie = WebUtils.getCookie(request, CustomCookieUtils.COOKIE_NAME_PHONE_VALIDATION_TOKEN);

        String phoneValidationToken = null;
        if (phoneValidationCookie == null) {
            throw new NotMatchedFormatException("인증번호가 정확하지 않습니다.");
        }

        phoneValidationToken = phoneValidationCookie.getValue();

        String jwtSecret = phoneNumber + phoneNumberValidationCode + ValidationTokenUtils.getJwtEmailValidationSecret();
        CustomJwtUtils.parseJwt(jwtSecret, phoneValidationToken, "인증번호가 정확하지 않습니다.");

        UserEntity userEntity = userService.searchById(userId);

        if (userEntity.getPhoneNumber() != null && userEntity.getPhoneNumber().equals(phoneNumber)) {
            return;
        }

        userEntity.setPhoneNumber(phoneNumber);
        userEntity.setUpdatedAt(CustomDateUtils.getCurrentDateTime());

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

    @Transactional
    public void changeEmail(HttpServletRequest request, HttpServletResponse response, String email, String emailValidationCode) {
        UUID userId = userService.getUserIdOrThrow();

        /*
        이메일 형식 체크
         */
        if (!DataFormatUtils.isPassEmailFormatValid(email)) {
            throw new NotMatchedFormatException("입력하신 이메일 형식이 정확한지 확인하여 주세요.");
        }

        /*
        이메일 인증번호 체크
         */
        Cookie emailValidationCookie = WebUtils.getCookie(request, CustomCookieUtils.COOKIE_NAME_EMAIL_VALIDATION_TOKEN);

        String emailValidationToken = null;
        if (emailValidationCookie == null) {
            throw new NotMatchedFormatException("인증번호가 정확하지 않습니다.");
        }

        emailValidationToken = emailValidationCookie.getValue();

        String jwtSecret = email + emailValidationCode + ValidationTokenUtils.getJwtEmailValidationSecret();
        CustomJwtUtils.parseJwt(jwtSecret, emailValidationToken, "인증번호가 정확하지 않습니다.");

        UserEntity userEntity = userService.searchById(userId);

        if (userEntity.getEmail() != null && userEntity.getEmail().equals(email)) {
            return;
        }

        userEntity.setEmail(email);
        userEntity.setUpdatedAt(CustomDateUtils.getCurrentDateTime());

        /*
        cp_phone_validation_token cookie 삭제
         */
        ResponseCookie emailValidationTokenCookie = ResponseCookie.from(CustomCookieUtils.COOKIE_NAME_EMAIL_VALIDATION_TOKEN, null)
                .domain(CustomCookieUtils.COOKIE_DOMAIN)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, emailValidationTokenCookie.toString());
    }

    @Transactional
    public void changePassword(HttpServletRequest request, HttpServletResponse response, String password, String newPassword, String newPasswordChecker) {
        UUID userId = userService.getUserIdOrThrow();

        if (!DataFormatUtils.isPassPasswordFormatValid(password)) {
            throw new NotMatchedFormatException("현재 비밀번호를 다시 확인해 주세요.");
        }

        if (!DataFormatUtils.isPassPasswordFormatValid(newPassword)) {
            throw new NotMatchedFormatException("비밀번호는 영문, 숫자, 특수문자 혼합 8-50자로 지정해 주세요.");
        }

        if (!newPassword.equals(newPasswordChecker)) {
            throw new NotMatchedFormatException("새 비밀번호를 다시 확인해 주세요.");
        }

        if (password.equals(newPassword)) {
            throw new NotMatchedFormatException("현재 비밀번호와 다른 비밀번호로 지정해 주세요.");
        }

        UserEntity userEntity = userService.searchById(userId);

        String salt = userEntity.getSalt();
        String storedPassword = userEntity.getPassword();

        if (!passwordEncoder.matches(password + salt, storedPassword)) {
            throw new NotMatchedFormatException("현재 비밀번호가 일치하지 않습니다.");
        }

        String newSalt = UUID.randomUUID().toString();
        String newEncPassword = passwordEncoder.encode(newPassword + newSalt);

        userEntity.setPassword(newEncPassword);
        userEntity.setSalt(newSalt);
        userEntity.setUpdatedAt(CustomDateUtils.getCurrentDateTime());

        this.logout(request, response);
        refreshTokenService.deleteAllByUserId(userId);
    }

    @Transactional
    public void changeProfileImageUri(String profileImageUri) {
        UUID userId = userService.getUserIdOrThrow();

        UserEntity userEntity = userService.searchById(userId);

        userEntity.setProfileImageUri(profileImageUri);
        userEntity.setUpdatedAt(CustomDateUtils.getCurrentDateTime());
    }

    @Transactional
    public void withdrawal(HttpServletRequest request, HttpServletResponse response, String password) {
        UUID userId = userService.getUserIdOrThrow();

        UserEntity userEntity = userService.searchById(userId);

        String salt = userEntity.getSalt();
        String storedPassword = userEntity.getPassword();

        if (!passwordEncoder.matches(password + salt, storedPassword)) {
            throw new NotMatchedFormatException("비밀번호가 일치하지 않습니다.");
        }

        userService.logicalDelete(userEntity);
        refreshTokenService.deleteAllByUserId(userId);

        if (userEntity.getRoomId() != null) {
            RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);
            roomService.logicalDelete(roomEntity);
        }
        this.logout(request, response);
    }
}
