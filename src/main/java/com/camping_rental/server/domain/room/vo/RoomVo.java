package com.camping_rental.server.domain.room.vo;

import com.camping_rental.server.domain.room.entity.RoomEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomVo {
    private Integer cid;
    private UUID id;
    private String name;
    private String phoneNumber;
    private String introduction;
    private String profileImageUri;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deletedFlag;
    private UUID userId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Basic{
        private UUID id;
        private String name;
        private String phoneNumber;
        private String introduction;
        private String profileImageUri;

        public static Basic toVo(RoomEntity entity){
            Basic vo = Basic.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .phoneNumber(entity.getPhoneNumber())
                    .introduction(entity.getIntroduction())
                    .profileImageUri(entity.getProfileImageUri())
                    .build();
            return vo;
        }
    }
}
