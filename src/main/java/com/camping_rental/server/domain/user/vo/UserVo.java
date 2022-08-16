package com.camping_rental.server.domain.user.vo;

import com.camping_rental.server.domain.user.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVo {
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
    public static class FindUsername{
        private String username;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime createdAt;
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
        private UUID roomId;

        public static UserVo.Basic toVo(UserEntity entity) {
            if(entity == null){
                return null;
            }

            UserVo.Basic dto = UserVo.Basic.builder()
                    .id(entity.getId())
                    .username(entity.getUsername())
                    .email(entity.getEmail())
                    .name(entity.getName())
                    .nickname(entity.getNickname())
                    .phoneNumber(entity.getPhoneNumber())
                    .roles(entity.getRoles())
                    .roomId(entity.getRoomId())
                    .build();
            return dto;
        }
    }
}
