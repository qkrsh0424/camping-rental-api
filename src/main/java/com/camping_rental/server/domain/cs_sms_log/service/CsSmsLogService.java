package com.camping_rental.server.domain.cs_sms_log.service;

import com.camping_rental.server.domain.cs_sms_log.entity.CsSmsLogEntity;
import com.camping_rental.server.domain.cs_sms_log.repository.CsSmsLogRepository;
import com.camping_rental.server.domain.room.entity.RoomEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CsSmsLogService {
    private final CsSmsLogRepository csSmsLogRepository;

    public void saveAndModify(CsSmsLogEntity csSmsLogEntity){
        csSmsLogRepository.save(csSmsLogEntity);
    }

    public List<CsSmsLogEntity> searchListByRentalOrderInfoIdAndRoomIdOrderByCidDesc(UUID rentalOrderInfoId, UUID roomId) {
        return csSmsLogRepository.findAllByRentalOrderInfoIdAndRoomIdOrderByCidDesc(rentalOrderInfoId, roomId);
    }
}
