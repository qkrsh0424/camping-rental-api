package com.camping_rental.server.domain.user.dto;

import com.camping_rental.server.domain.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.UUID;

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
        private String email;
        private String emailValidationCode;
        private String password;
        private String passwordChecker;
        private String nickname;
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
    public static class Basic {
        private UUID id;
        private String username;
        private String email;
        private String name;
        private String nickname;
        private String phoneNumber;
        private String roles;

        public static Basic toDto(UserEntity entity) {
            if(entity == null){
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
