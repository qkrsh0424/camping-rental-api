package com.camping_rental.server.domain.cs_sms_log.service;

import com.camping_rental.server.domain.cs_sms_log.entity.CsSmsLogEntity;
import com.camping_rental.server.domain.cs_sms_log.vo.CsSmsLogVo;
import com.camping_rental.server.domain.rental_order_info.entity.RentalOrderInfoEntity;
import com.camping_rental.server.domain.rental_order_info.service.RentalOrderInfoService;
import com.camping_rental.server.domain.room.entity.RoomEntity;
import com.camping_rental.server.domain.room.service.RoomService;
import com.camping_rental.server.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CsSmsLogBusinessService {
    private final UserService userService;
    private final RoomService roomService;
    private final RentalOrderInfoService rentalOrderInfoService;
    private final CsSmsLogService csSmsLogService;

    @Transactional(readOnly = true)
    public List<CsSmsLogVo.Basic> searchList(UUID rentalOrderInfoId) {
        UUID userId = userService.getUserIdOrThrow();

        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);
        UUID roomId = roomEntity.getId();

        List<CsSmsLogEntity> csSmsLogEntities = csSmsLogService.searchListByRentalOrderInfoIdAndRoomIdOrderByCidDesc(rentalOrderInfoId, roomId);
        List<CsSmsLogVo.Basic> csSmsLogVos = csSmsLogEntities.stream().map(CsSmsLogVo.Basic::toVo).collect(Collectors.toList());
        return csSmsLogVos;
    }
}
