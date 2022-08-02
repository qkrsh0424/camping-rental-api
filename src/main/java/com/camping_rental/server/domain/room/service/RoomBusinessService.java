package com.camping_rental.server.domain.room.service;

import com.camping_rental.server.domain.enums.DeletedFlagEnums;
import com.camping_rental.server.domain.exception.dto.AccessDeniedPermissionException;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.room.dto.RoomDto;
import com.camping_rental.server.domain.room.entity.RoomEntity;
import com.camping_rental.server.domain.room.vo.RoomVo;
import com.camping_rental.server.domain.twilio.dto.TwilioSmsRequestDto;
import com.camping_rental.server.domain.twilio.service.TwilioSmsService;
import com.camping_rental.server.domain.twilio.strategy.RoomSms;
import com.camping_rental.server.domain.twilio.strategy.TwilioSmsRequestFactory;
import com.camping_rental.server.domain.user.entity.UserEntity;
import com.camping_rental.server.domain.user.service.UserService;
import com.camping_rental.server.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class RoomBusinessService {
    private final UserService userService;
    private final RoomService roomService;
    private final TwilioSmsService twilioSmsService;

    @Transactional(readOnly = true)
    public Object searchOne(UUID id) {
        RoomEntity roomEntity = roomService.searchByIdOrThrow(id);

        RoomVo.Basic roomVo = RoomVo.Basic.toVo(roomEntity);
        return roomVo;
    }

    @Transactional
    public void createOne(
            HttpServletRequest request,
            HttpServletResponse response,
            RoomDto.Create roomDto
    ) {
        UUID USER_ID = userService.getUserIdOrThrow();
        RoomDto.Create.checkFieldValid(roomDto);

        String name = roomDto.getName();
        String phoneNumber = roomDto.getPhoneNumber();
        String phoneValidationCode = roomDto.getPhoneValidationCode();

        /*
        룸 중복 생성 여부 확인
         */
        RoomEntity duplicatedChecker = roomService.searchByUserIdOrNull(USER_ID);
        if (duplicatedChecker != null) {
            throw new NotMatchedFormatException("룸의 중복 생성은 불가능 합니다.");
        }

        /*
        휴대폰 인증번호 체크
         */
        Cookie phoneValidationCookie = WebUtils.getCookie(request, CustomCookieUtils.COOKIE_NAME_PHONE_VALIDATION_TOKEN);

        String phoneValidationToken = null;
        if (phoneValidationCookie == null) {
            throw new NotMatchedFormatException("인증번호가 정확하지 않습니다.");
        }

        phoneValidationToken = phoneValidationCookie.getValue();

        String jwtSecret = phoneNumber + phoneValidationCode + ValidationTokenUtils.getJwtPhoneValidationSecret();
        CustomJwtUtils.parseJwt(jwtSecret, phoneValidationToken, "인증번호가 정확하지 않습니다.");

        /*
        룸 이름 중복 체크
         */
        RoomEntity roomDuplicateChecker = roomService.searchByName(name);
        if (roomDuplicateChecker != null) {
            throw new NotMatchedFormatException("이미 사용중인 룸 이름 입니다.");
        }

        UUID roomId = UUID.randomUUID();
        RoomEntity roomEntity = RoomEntity.builder()
                .cid(null)
                .id(roomId)
                .name(name)
                .phoneNumber(phoneNumber)
                .introduction("")
                .createdAt(CustomDateUtils.getCurrentDateTime())
                .updatedAt(CustomDateUtils.getCurrentDateTime())
                .deletedFlag(DeletedFlagEnums.EXIST.getValue())
                .userId(USER_ID)
                .build();

        roomService.saveAndModify(roomEntity);

        /*
        UserEntity 의 roomId를 업데이트 해준다.
         */
        UserEntity userEntity = userService.searchById(USER_ID);
        userEntity.setRoomId(roomId);

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
    public void changeIntroduction(UUID roomId, String introduction) {
        UUID USER_ID = userService.getUserIdOrThrow();
        RoomEntity roomEntity = roomService.searchByIdOrThrow(roomId);

        if (introduction.length() > 400) {
            throw new NotMatchedFormatException("소개말은 최대 400자 까지 작성할 수 있습니다.");
        }

        if (!roomEntity.getUserId().equals(USER_ID)) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        /*
        Dirty Check Update
         */
        roomEntity.setIntroduction(introduction);
        roomEntity.setUpdatedAt(CustomDateUtils.getCurrentDateTime());
    }

    @Transactional
    public void changeName(UUID roomId, String name) {
        UUID USER_ID = userService.getUserIdOrThrow();
        RoomEntity roomEntity = roomService.searchByIdOrThrow(roomId);
        /*
        룸네임 체크
         */
        long nameMatcherCount = Pattern.compile("^[\\s]|[\\s]$").matcher(name).results().count();
        if (nameMatcherCount != 0 || name.length() < 2 || name.length() > 15) {
            throw new NotMatchedFormatException("룸 이름을 정확하게 입력해 주세요. 2-15 글자 내로 첫 글자와 마지막 글자에 공백은 허용되지 않습니다.");
        }

        if (!roomEntity.getUserId().equals(USER_ID)) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        /*
        변경하려는 네임과 기존의 네임이 같다면 변경한척만 해서, 다음 로직을 수행 못하게 한다.
         */
        if(roomEntity.getName().equals(name)){
            return;
        }

        /*
        룸네임 중복 체크
         */
        RoomEntity duplicateChecker = roomService.searchByName(name);
        if (duplicateChecker != null) {
            throw new NotMatchedFormatException("이미 사용중인 이름 입니다.");
        }

        /*
        Dirty Check Update
         */
        roomEntity.setName(name);
        roomEntity.setUpdatedAt(CustomDateUtils.getCurrentDateTime());
    }

    @Transactional
    public void changePhoneNumber(HttpServletRequest request, HttpServletResponse response, UUID roomId, String phoneNumber, String phoneValidationCode) {
        UUID USER_ID = userService.getUserIdOrThrow();
        RoomEntity roomEntity = roomService.searchByIdOrThrow(roomId);

        /*
        권한 체크
         */
        if (!roomEntity.getUserId().equals(USER_ID)) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        /*
        휴대폰 인증번호 체크
         */
        Cookie phoneValidationCookie = WebUtils.getCookie(request, CustomCookieUtils.COOKIE_NAME_PHONE_VALIDATION_TOKEN);

        String phoneValidationToken = null;
        if (phoneValidationCookie == null) {
            throw new NotMatchedFormatException("인증번호가 정확하지 않습니다.");
        }

        phoneValidationToken = phoneValidationCookie.getValue();

        String jwtSecret = phoneNumber + phoneValidationCode + ValidationTokenUtils.getJwtPhoneValidationSecret();
        CustomJwtUtils.parseJwt(jwtSecret, phoneValidationToken, "인증번호가 정확하지 않습니다.");

        /*
        변경하려는 휴대폰번호와 기존의 휴대폰번호가 같다면 변경한척만 해서, 다음 로직을 수행 못하게 한다.
         */
        if(roomEntity.getPhoneNumber().equals(phoneNumber)){
            return;
        }

        /*
        휴대폰 번호 중복 체크
         */
        RoomEntity duplicateChecker = roomService.searchByPhoneNumber(phoneNumber);
        if(duplicateChecker != null){
            throw new NotMatchedFormatException("이미 사용중인 휴대폰 번호 입니다.");
        }

        /*
        Dirty check update
         */
        roomEntity.setPhoneNumber(phoneNumber);
        roomEntity.setUpdatedAt(CustomDateUtils.getCurrentDateTime());
    }

    @Transactional(readOnly = true)
    public void sendPhoneValidationCode(HttpServletResponse response, String phoneNumber) {

        /*
        입력받은 phoneNumber의 포맷 형식을 확인한다.
         */
        DataFormatUtils.checkPhoneNumberFormat(phoneNumber);

        /*
        입력받은 phoneNumber 와 일치하는 RoomEntity 찾는다. 만약에 해당 데이터가 있다면 return
         */
        RoomEntity roomEntity = roomService.searchByPhoneNumber(phoneNumber);
        if (roomEntity != null) {
            return;
        }

        /*
        인증번호와 인증번호 토큰을 만든다.
         */
        String validationNum = ValidationTokenUtils.generate6DigitValidationCode();
        String validationNumToken = ValidationTokenUtils.generatePhoneValidationNumberJwtToken(validationNum, phoneNumber);

        /*
        인증번호 토큰을 cookie로 내보내준다.
         */
        ResponseCookie phoneAuthToken = ResponseCookie.from(CustomCookieUtils.COOKIE_NAME_PHONE_VALIDATION_TOKEN, validationNumToken)
                .secure(CustomCookieUtils.SECURE)
                .domain(CustomCookieUtils.COOKIE_DOMAIN)
                .httpOnly(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(CustomCookieUtils.PHONE_VALIDATION_COOKIE_EXPIRATION)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, phoneAuthToken.toString());

        /*
        인증 코드를 담은 smsMessage를 트윌리오를 통해 전송한다.
         */
        TwilioSmsRequestFactory twilioSmsRequestFactory = new TwilioSmsRequestFactory(new RoomSms.ValidationCode());
        TwilioSmsRequestDto smsRequestDto = twilioSmsRequestFactory.make(Map.of(
                "validationNum", validationNum,
                "smsReceiverPhoneNumber", phoneNumber
        ));

        twilioSmsService.sendSmsAsync(smsRequestDto);
    }
}
