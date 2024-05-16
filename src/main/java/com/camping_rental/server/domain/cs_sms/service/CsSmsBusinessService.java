package com.camping_rental.server.domain.cs_sms.service;

import com.camping_rental.server.domain.cs_sms_log.entity.CsSmsLogEntity;
import com.camping_rental.server.domain.cs_sms_log.service.CsSmsLogService;
import com.camping_rental.server.domain.enums.DeletedFlagEnums;
import com.camping_rental.server.domain.exception.dto.AccessDeniedPermissionException;
import com.camping_rental.server.domain.naver.sens.dto.NaverCloudSensSmsSendDto;
import com.camping_rental.server.domain.naver.sens.service.NaverCloudSensSmsService;
import com.camping_rental.server.domain.rental_order_info.entity.RentalOrderInfoEntity;
import com.camping_rental.server.domain.rental_order_info.service.RentalOrderInfoService;
import com.camping_rental.server.domain.room.entity.RoomEntity;
import com.camping_rental.server.domain.room.service.RoomService;
import com.camping_rental.server.domain.user.service.UserService;
import com.camping_rental.server.utils.CustomDateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CsSmsBusinessService {
    private final UserService userService;
    private final RoomService roomService;
    private final RentalOrderInfoService rentalOrderInfoService;
    private final CsSmsLogService csSmsLogService;
    private final NaverCloudSensSmsService naverCloudSensSmsService;

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

        List<NaverCloudSensSmsSendDto> naverCloudSensSmsSendDtoList = new ArrayList<>();
        naverCloudSensSmsSendDtoList.add(
                NaverCloudSensSmsSendDto.toDto(borrowerPhoneNumber, smsMessage, "[Campal | 캠핑 렌탈]")
        );
        naverCloudSensSmsService.sendMultiple(naverCloudSensSmsSendDtoList);
    }
}
