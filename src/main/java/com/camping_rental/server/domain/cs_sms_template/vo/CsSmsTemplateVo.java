package com.camping_rental.server.domain.cs_sms_template.vo;

import com.camping_rental.server.domain.cs_sms_template.entity.CsSmsTemplateEntity;
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
public class CsSmsTemplateVo {
    private Long cid;
    private UUID id;
    private String name;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deletedFlag;
    private UUID roomId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Basic {
        private UUID id;
        private String name;
        private String message;
        private UUID roomId;

        public static Basic toVo(CsSmsTemplateEntity entity) {
            Basic vo = Basic.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .message(entity.getMessage())
                    .roomId(entity.getRoomId())
                    .build();

            return vo;
        }
    }
}
