package com.camping_rental.server.domain.user.dto;

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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LocalSignup{
        private String username;
        private String password;
        private String passwordChecker;
        private String phoneNumber;
        private String phoneNumberValidationCode;
    }
}
