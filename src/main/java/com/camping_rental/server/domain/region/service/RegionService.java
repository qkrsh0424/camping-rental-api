package com.camping_rental.server.domain.region.service;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.region.entity.RegionEntity;
import com.camping_rental.server.domain.region.enums.RegionDeletedFlagEnum;
import com.camping_rental.server.domain.region.projection.RegionProjection;
import com.camping_rental.server.domain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final RegionRepository regionRepository;

    public void saveAndModify(RegionEntity regionEntity) {
        regionRepository.save(regionEntity);
    }

    public List<RegionEntity> searchList(UUID createdBy) {
        return regionRepository.findByCreatedBy(createdBy);
    }

    public RegionEntity searchByIdOrThrow(UUID id) {
        return regionRepository.findById(id).orElseThrow(()-> new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다."));
    }

    public void logicalDelete(RegionEntity regionEntity) {
        regionEntity.setDeletedFlag(RegionDeletedFlagEnum.DELETED.getValue());
    }

    public List<RegionEntity> searchListByRoomId(UUID roomId) {
        return regionRepository.findByRoomId(roomId);
    }

    public long countByRoomId(UUID roomId) {
        return regionRepository.countByRoomId(roomId.toString());
    }

    public List<RegionProjection.SidoAndSigungus> qSearchListSidoWithSigungus() {
        List<RegionProjection.SidoAndSigungus> sidoAndSigungusProjections = regionRepository.qSelectListSidoAndSigungus();
        return sidoAndSigungusProjections;
    }
}
