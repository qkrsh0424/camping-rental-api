package com.camping_rental.server.domain.cs_sms.service;

import com.camping_rental.server.domain.cs_sms_log.entity.CsSmsLogEntity;
import com.camping_rental.server.domain.cs_sms_log.service.CsSmsLogService;
import com.camping_rental.server.domain.enums.DeletedFlagEnums;
import com.camping_rental.server.domain.exception.dto.AccessDeniedPermissionException;
import com.camping_rental.server.domain.rental_order_info.entity.RentalOrderInfoEntity;
import com.camping_rental.server.domain.rental_order_info.service.RentalOrderInfoService;
import com.camping_rental.server.domain.room.entity.RoomEntity;
import com.camping_rental.server.domain.room.service.RoomService;
import com.camping_rental.server.domain.twilio.dto.TwilioSmsRequestDto;
import com.camping_rental.server.domain.twilio.service.TwilioSmsService;
import com.camping_rental.server.domain.user.service.UserService;
import com.camping_rental.server.utils.CustomDateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CsSmsBusinessService {
    private final UserService userService;
    private final RoomService roomService;
    private final RentalOrderInfoService rentalOrderInfoService;
    private final TwilioSmsService twilioSmsService;
    private final CsSmsLogService csSmsLogService;

    @Transactional
    public void sendSmsForRentalOrderInfo(UUID rentalOrderInfoId, String smsMessage) {
        UUID userId = userService.getUserIdOrThrow();
        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);
        RentalOrderInfoEntity rentalOrderInfoEntity = rentalOrderInfoService.searchByIdOrThrow(rentalOrderInfoId);

        if (!rentalOrderInfoEntity.getLenderRoomId().equals(roomEntity.getId())) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        String borrowerPhoneNumber = rentalOrderInfoEntity.getBorrowerPhoneNumber();
        String senderPhoneNumber = roomEntity.getPhoneNumber();
        UUID roomId = roomEntity.getId();
        UUID csSmsLogId = UUID.randomUUID();

        List<TwilioSmsRequestDto> twilioSmsRequestDtos = new ArrayList<>();

        twilioSmsRequestDtos.add(
                TwilioSmsRequestDto.toDto(
                        borrowerPhoneNumber,
                        smsMessage
                )
        );

        CsSmsLogEntity csSmsLogEntity = CsSmsLogEntity.builder()
                .cid(null)
                .id(csSmsLogId)
                .smsMessage(smsMessage)
                .fromPhoneNumber(senderPhoneNumber)
                .toPhoneNumber(borrowerPhoneNumber)
                .createdBy(userId)
                .createdAt(CustomDateUtils.getCurrentDateTime())
                .deletedFlag(DeletedFlagEnums.EXIST.getValue())
                .rentalOrderInfoId(rentalOrderInfoId)
                .roomId(roomId)
                .build();

        csSmsLogService.saveAndModify(csSmsLogEntity);
        twilioSmsService.sendMultipleSms(twilioSmsRequestDtos);
    }
}
