package com.camping_rental.server.domain.user.service;

import com.camping_rental.server.domain.exception.dto.InvalidUserException;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.refresh_token.entity.RefreshTokenEntity;
import com.camping_rental.server.domain.refresh_token.service.RefreshTokenService;
import com.camping_rental.server.domain.user.dto.UserDto;
import com.camping_rental.server.domain.user.entity.UserEntity;
import com.camping_rental.server.domain.user.enums.UserAllowedAccessCountEnum;
import com.camping_rental.server.domain.user.enums.UserLoginTypeEnum;
import com.camping_rental.server.domain.user.enums.UserRoleEnum;
import com.camping_rental.server.domain.user.vo.UserVo;
import com.camping_rental.server.utils.*;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserBusinessService {
    @Value("${app.jwt.phone-validation.secret}")
    private String JWT_PHONE_VALIDATION_SECRET;
    private final PasswordEncoder passwordEncoder;

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public void checkDuplicateUsername(String username) {
        UserEntity userEntity = userService.searchByUsername(username);

        if (userEntity != null) {
            throw new NotMatchedFormatException("이미 사용중인 아이디 입니다.");
        }
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
            throw new InvalidUserException("로그인이 만료 되었습니다.");
        }

        /*
        RefreshTokenEntity 가 가지고 있는 jwt 값을 파싱한다. 파싱 에러가 난다면 401 에러
         */
        Claims refreshTokenClaims = null;
        try {
            refreshTokenClaims = CustomJwtUtils.parseJwt(AuthTokenUtils.getRefreshTokenSecret(), refreshTokenEntity.getRefreshToken());
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new InvalidUserException("로그인이 만료 되었습니다.");
        } catch (Exception e) {
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

    public Object searchUserInfo(HttpServletRequest request) {
        Cookie jwtTokenCookie = WebUtils.getCookie(request, CustomCookieUtils.COOKIE_NAME_ACCESS_TOKEN);
        if(jwtTokenCookie == null){
            return null;
        }

        UUID userId = userService.getUserIdOrThrow();

        if(userId == null){
            return null;
        }

        UserEntity userEntity = userService.searchById(userId);

        if (userEntity == null) {
            return null;
        }

        UserVo.Basic userDto = UserVo.Basic.toVo(userEntity);

        return userDto;
    }

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
}
