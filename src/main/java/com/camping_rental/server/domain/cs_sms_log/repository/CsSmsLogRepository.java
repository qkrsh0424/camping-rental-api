package com.camping_rental.server.domain.cs_sms_log.repository;

import com.camping_rental.server.domain.cs_sms_log.entity.CsSmsLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CsSmsLogRepository extends JpaRepository<CsSmsLogEntity, Long> {
    List<CsSmsLogEntity> findAllByRentalOrderInfoId(UUID rentalOrderInfoId);

    List<CsSmsLogEntity> findAllByRentalOrderInfoIdAndRoomIdOrderByCidDesc(UUID rentalOrderInfoId, UUID roomId);
}
