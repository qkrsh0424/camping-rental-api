package com.camping_rental.server.domain.rental_order_info.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RentalOrderInfoUpdateDto {
    private String id;
    private String orderNumber;
    private String orderer;
    private String ordererPhoneNumber;
    private String borrower;
    private String borrowerPhoneNumber;
    private LocalDateTime pickupDate;
    private String pickupTime;
    private String pickupPlace;
    private LocalDateTime returnDate;
    private String returnTime;
    private String returnPlace;
    private String ordererType;
    private String csMemo;
    private String serviceAgreementYn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deletedFlag;
    private String ordererId;
    private String lenderRoomId;
}
