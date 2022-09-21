package com.camping_rental.server.domain.cs_sms_log.vo;

import com.camping_rental.server.domain.cs_sms_log.entity.CsSmsLogEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.UUID;

public class CsSmsLogVo {
    private Long cid;
    private UUID id;
    private String smsMessage;
    private String fromPhoneNumber;
    private String toPhoneNumber;
    private UUID createdBy;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;
    private boolean deletedFlag;
    private UUID rentalOrderInfoId;
    private UUID roomId;

    @Getter
    @ToString
    @AllArgsConstructor
    @Builder
    public static class Basic {
        private UUID id;
        private String smsMessage;
        private String fromPhoneNumber;
        private String toPhoneNumber;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime createdAt;
        private UUID rentalOrderInfoId;
        private UUID roomId;

        public static Basic toVo(CsSmsLogEntity entity) {
            Basic vo = Basic.builder()
                    .id(entity.getId())
                    .smsMessage(entity.getSmsMessage())
                    .fromPhoneNumber(entity.getFromPhoneNumber())
                    .toPhoneNumber(entity.getToPhoneNumber())
                    .createdAt(entity.getCreatedAt())
                    .rentalOrderInfoId(entity.getRentalOrderInfoId())
                    .roomId(entity.getRoomId())
                    .build();
            return vo;
        }
    }
}
