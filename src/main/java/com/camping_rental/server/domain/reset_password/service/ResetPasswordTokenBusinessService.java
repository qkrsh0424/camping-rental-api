package com.camping_rental.server.domain.reset_password.service;

import com.camping_rental.server.domain.enums.DeletedFlagEnums;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.refresh_token.service.RefreshTokenService;
import com.camping_rental.server.domain.reset_password.dto.ResetPasswordTokenDto;
import com.camping_rental.server.domain.reset_password.entity.ResetPasswordTokenEntity;
import com.camping_rental.server.domain.reset_password.vo.ResetPasswordTokenVo;
import com.camping_rental.server.domain.user.entity.UserEntity;
import com.camping_rental.server.domain.user.service.UserService;
import com.camping_rental.server.utils.CustomCookieUtils;
import com.camping_rental.server.utils.CustomDateUtils;
import com.camping_rental.server.utils.DataFormatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResetPasswordTokenBusinessService {
    private final ResetPasswordTokenService resetPasswordTokenService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public Object create(
            HttpServletRequest request,
            HttpServletResponse response,
            ResetPasswordTokenDto.Create resetPasswordTokenDto
    ) {
        ResetPasswordTokenDto.Create.checkFormValid(request, resetPasswordTokenDto);

        UUID id = UUID.randomUUID();
        String username = resetPasswordTokenDto.getUsername();
        LocalDateTime createdAt = CustomDateUtils.getCurrentDateTime();
        LocalDateTime expiredAt = createdAt.plusMinutes(10);

        ResetPasswordTokenEntity resetPasswordTokenEntity = ResetPasswordTokenEntity.builder()
                .cid(null)
                .id(id)
                .username(username)
                .createdAt(createdAt)
                .expiredAt(expiredAt)
                .deletedFlag(DeletedFlagEnums.EXIST.getValue())
                .build();

        resetPasswordTokenService.saveAndModify(resetPasswordTokenEntity);

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

        ResetPasswordTokenVo.Create vo = ResetPasswordTokenVo.Create.builder()
                .resetToken(id)
                .build();

        return vo;
    }

    @Transactional(readOnly = true)
    public String checkTokenValid(UUID id) {
        ResetPasswordTokenEntity entity = resetPasswordTokenService.searchByIdOrNull(id);
        LocalDateTime currentTime = CustomDateUtils.getCurrentDateTime();

        if(entity != null && currentTime.isBefore(entity.getExpiredAt())){
            return "valid";
        }

        return "expired";
    }

    @Transactional
    public void changePassword(
            HttpServletResponse response,
            UUID resetTokenId,
            String password,
            String passwordChecker
    ) {
        /*
        password 형식 체크
         */
        if (!DataFormatUtils.isPassPasswordFormatValid(password)) {
            throw new NotMatchedFormatException("입력하신 패스워드 형식이 정확한지 확인하여 주세요.");
        }

        /*
        password, passwordChecker 동일성 체크
         */
        if (!password.equals(passwordChecker)) {
            throw new NotMatchedFormatException("입력하신 패스워드를 다시 확인하여 주세요.");
        }

        ResetPasswordTokenEntity resetPasswordTokenEntity = resetPasswordTokenService.searchByIdOrNull(resetTokenId);
        if(resetPasswordTokenEntity == null){
            throw new NotMatchedFormatException("유효하지 않은 토큰입니다.");
        }

        if(resetPasswordTokenEntity.getExpiredAt().isBefore(CustomDateUtils.getCurrentDateTime())){
            throw new NotMatchedFormatException("유효하지 않은 토큰입니다.");
        }

        UserEntity userEntity = userService.searchByUsernameOrNull(resetPasswordTokenEntity.getUsername());

        String salt = UUID.randomUUID().toString();
        String encPassword = passwordEncoder.encode(password + salt);

        /*
        Dirty checking update
         */
        resetPasswordTokenEntity.setDeletedFlag(DeletedFlagEnums.DELETED.getValue());

        userEntity.setPassword(encPassword);
        userEntity.setSalt(salt);
        userEntity.setUpdatedAt(CustomDateUtils.getCurrentDateTime());

        refreshTokenService.deleteAllByUserId(userEntity.getId());

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
