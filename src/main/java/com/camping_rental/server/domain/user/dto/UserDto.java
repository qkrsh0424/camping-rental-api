package com.camping_rental.server.domain.user.dto;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.user.entity.UserEntity;
import com.camping_rental.server.domain.user.service.UserService;
import com.camping_rental.server.utils.CustomCookieUtils;
import com.camping_rental.server.utils.CustomJwtUtils;
import com.camping_rental.server.utils.DataFormatUtils;
import com.camping_rental.server.utils.ValidationTokenUtils;
import lombok.*;
import org.springframework.web.util.WebUtils;

import javax.persistence.Column;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Integer cid;
    private UUID id;
    private String loginType;
    private String socialPlatform;
    private String socialPlatformId;
    private String username;
    private String password;
    private String salt;
    private String email;
    private String name;
    private String nickname;
    private String phoneNumber;
    private String profileImageUri;
    private String roles;
    private Integer allowedAccessCount;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private boolean deletedFlag;
    private UUID roomId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LocalSignup {
        private String username;
        private String password;
        private String passwordChecker;
        private String nickname;
        private String phoneNumber;
        private String phoneNumberValidationCode;
        private String serviceTermsYn;
        private String privacyPolicyYn;
        private String marketingYn;

        public static void checkFormValid(
                HttpServletRequest request,
                LocalSignup dto,
                UserService userService
        ) {
            /*
            serviceTermsYn | privacyPolicyYn 체크
             */
            if(!dto.getServiceTermsYn().equals("y") || !dto.getPrivacyPolicyYn().equals("y")){
                throw new NotMatchedFormatException("서비스 이용약관 및 서비스 개인정보취급방침의 동의는 필수 입니다.");
            }

            /*
            username 형식 체크
             */
            if(!DataFormatUtils.isPassUsernameFormatValid(dto.getUsername())){
                throw new NotMatchedFormatException("입력하신 아이디 형식이 정확한지 확인하여 주세요.");
            }

            /*
            password 형식 체크
             */
            if (!DataFormatUtils.isPassPasswordFormatValid(dto.getPassword())) {
                throw new NotMatchedFormatException("입력하신 패스워드 형식이 정확한지 확인하여 주세요.");
            }

            /*
            password, passwordChecker 동일성 체크
             */
            if (!dto.getPassword().equals(dto.getPasswordChecker())) {
                throw new NotMatchedFormatException("입력하신 패스워드를 다시 확인하여 주세요.");
            }

            if (!DataFormatUtils.isPassNicknameFormatValid(dto.getNickname())) {
                throw new NotMatchedFormatException("입력하신 이름 형식이 정확한지 확인하여 주세요.");
            }

            /*
            휴대전화 형식 체크
             */
            if (!DataFormatUtils.isPassPhoneNumberFormatValid(dto.getPhoneNumber())) {
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

            String jwtSecret = dto.getPhoneNumber() + dto.getPhoneNumberValidationCode() + ValidationTokenUtils.getJwtEmailValidationSecret();
            CustomJwtUtils.parseJwt(jwtSecret, phoneValidationToken, "인증번호가 정확하지 않습니다.");

            /*
            username 중복 체크
             */
            if (userService.searchByUsernameOrNull(dto.getUsername()) != null) {
                throw new NotMatchedFormatException("이미 사용중인 아이디 입니다.");
            }
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LocalLogin {
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FindUsername {
        private String phoneNumber;
        private String phoneNumberValidationCode;

        public static void checkFormValid(HttpServletRequest request, FindUsername dto){
            /*
            휴대전화 인증번호 체크
             */
            Cookie phoneValidationCookie = WebUtils.getCookie(request, CustomCookieUtils.COOKIE_NAME_PHONE_VALIDATION_TOKEN);

            String phoneValidationToken = null;
            if (phoneValidationCookie == null) {
                throw new NotMatchedFormatException("인증번호가 정확하지 않습니다.");
            }

            phoneValidationToken = phoneValidationCookie.getValue();

            String jwtSecret = dto.phoneNumber + dto.phoneNumberValidationCode + ValidationTokenUtils.getJwtEmailValidationSecret();
            CustomJwtUtils.parseJwt(jwtSecret, phoneValidationToken, "인증번호가 정확하지 않습니다.");
        }

        public static String returnEncUsername(String username){
            Integer usernameLength = username.length();
            Integer starCount = (int) Math.floor(usernameLength / 2) - 1;
            Set<Integer> starIndex = new HashSet<>();
            List<String> usernameList = Arrays.asList(username.split(""));
            StringBuilder encUsername = new StringBuilder();

            if(starCount < 2){
                starCount = 2;
            }

            for(int i = 0; i < starCount; i++){
                Integer idx = (int) (Math.random()*usernameLength);
                while(!starIndex.add(idx)){
                    idx = (int) (Math.random()*usernameLength);
                }
            }

            for(int i = 0; i< usernameLength; i++){
                if(starIndex.contains(i)){
                    encUsername.append("*");
                }else{
                    encUsername.append(usernameList.get(i));
                }
            }

            return encUsername.toString();
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Basic {
        private UUID id;
        private String username;
        private String email;
        private String name;
        private String nickname;
        private String phoneNumber;
        private String profileImageUri;
        private String roles;

        public static Basic toDto(UserEntity entity) {
            if (entity == null) {
                return null;
            }

            Basic dto = Basic.builder()
                    .id(entity.getId())
                    .username(entity.getUsername())
                    .email(entity.getEmail())
                    .name(entity.getName())
                    .nickname(entity.getNickname())
                    .phoneNumber(entity.getPhoneNumber())
                    .roles(entity.getRoles())
                    .build();
            return dto;
        }
    }
}
