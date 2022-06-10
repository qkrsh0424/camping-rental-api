package com.camping_rental.server.domain.order_info.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInfoDto {
    private Integer cid;
    private UUID id;
    private String status;
    private String orderer;
    private String ordererPhoneNumber;
    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime pickupDate;
    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime returnDate;
    private String pickupRegion;
    private String returnRegion;
    private String pickupTime;
    private String returnTime;
    private String serviceAgreementYn;
    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderStatus {
        private UUID orderInfoId;
        private String status;
        private String selectedStatus;
        private String smsMessage;
        private boolean smsFlag;
    }
}
