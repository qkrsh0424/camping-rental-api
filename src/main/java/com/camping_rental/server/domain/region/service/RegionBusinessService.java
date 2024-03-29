package com.camping_rental.server.domain.region.service;

import com.camping_rental.server.domain.exception.dto.AccessDeniedPermissionException;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.region.dto.RegionDto;
import com.camping_rental.server.domain.region.entity.RegionEntity;
import com.camping_rental.server.domain.region.enums.RegionDeletedFlagEnum;
import com.camping_rental.server.domain.region.vo.RegionVo;
import com.camping_rental.server.domain.room.entity.RoomEntity;
import com.camping_rental.server.domain.room.service.RoomService;
import com.camping_rental.server.domain.user.service.UserService;
import com.camping_rental.server.utils.CustomDateUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionBusinessService {
    private final UserService userService;
    private final RegionService regionService;
    private final RoomService roomService;

    @Transactional(readOnly = true)
    public Object searchListByRoomId(UUID roomId) {
        List<RegionEntity> regionEntities = regionService.searchListByRoomId(roomId);

        List<RegionVo.Basic> regionVos = regionEntities.stream().map(entity -> {
            return RegionVo.Basic.toVo(entity);
        }).collect(Collectors.toList());

        return regionVos;
    }

    @Transactional(readOnly = true)
    public Object searchListSidoAndSigungus() {
        return regionService.qSearchListSidoWithSigungus();
    }

    @Transactional
    public void create(RegionDto.Create regionDto) {
        UUID id = UUID.randomUUID();
        UUID USER_ID = userService.getUserIdOrThrow();
        StringBuilder FULL_ADDRESS = new StringBuilder();
        FULL_ADDRESS.append(regionDto.getUserSelectedAddress());

        if (regionDto.getSido().isEmpty() || regionDto.getSigungu().isEmpty() || regionDto.getUserSelectedAddress().isEmpty()) {
            throw new NotMatchedFormatException("장소를 정확하게 입력해 주세요.");
        }

        if (!StringUtils.isBlank(regionDto.getAddressDetail())) {
            FULL_ADDRESS.append(" ");
            FULL_ADDRESS.append(regionDto.getAddressDetail());
        }

        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(USER_ID);
        long regionCount = regionService.countByRoomId(roomEntity.getId());

        if (regionCount >= 3) {
            throw new NotMatchedFormatException("장소는 최대 3개 까지만 등록 가능합니다.");
        }

        RegionEntity regionEntity = RegionEntity.builder()
                .cid(null)
                .id(id)
                .sido(regionDto.getSido())
                .sigungu(regionDto.getSigungu())
                .jibunAddress(regionDto.getJibunAddress())
                .roadAddress(regionDto.getRoadAddress())
                .buildingName(regionDto.getBuildingName())
                .address(regionDto.getAddress())
                .userSelectedAddress(regionDto.getUserSelectedAddress())
                .addressDetail(regionDto.getAddressDetail())
                .fullAddress(FULL_ADDRESS.toString())
                .bname(regionDto.getBname())
                .bname1(regionDto.getBname1())
                .bname2(regionDto.getBname2())
                .createdAt(CustomDateUtils.getCurrentDateTime())
                .updatedAt(CustomDateUtils.getCurrentDateTime())
                .deletedFlag(RegionDeletedFlagEnum.EXIST.getValue())
                .createdBy(roomEntity.getUserId())
                .roomCid(roomEntity.getCid())
                .roomId(roomEntity.getId())
                .build();

        regionService.saveAndModify(regionEntity);
    }

    @Transactional
    public void delete(UUID id) {
        UUID USER_ID = userService.getUserIdOrThrow();

        RegionEntity regionEntity = regionService.searchByIdOrThrow(id);

        if (!regionEntity.getCreatedBy().equals(USER_ID)) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        /*
        Dirty Check update
         */
        regionService.logicalDelete(regionEntity);
    }

    @Transactional
    public void update(UUID id, RegionDto regionDto) {
        UUID USER_ID = userService.getUserIdOrThrow();

        RegionEntity regionEntity = regionService.searchByIdOrThrow(id);

        if (!regionEntity.getCreatedBy().equals(USER_ID)) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        StringBuilder FULL_ADDRESS = new StringBuilder();
        FULL_ADDRESS.append(regionDto.getUserSelectedAddress());

        if (regionDto.getSido().isEmpty() || regionDto.getSigungu().isEmpty() || regionDto.getUserSelectedAddress().isEmpty()) {
            throw new NotMatchedFormatException("장소를 정확하게 입력해 주세요.");
        }

        if (!regionDto.getAddressDetail().isEmpty()) {
            FULL_ADDRESS.append(" ");
            FULL_ADDRESS.append(regionDto.getAddressDetail());
        }

        /*
        Dirty Check update
         */
        regionEntity.setSido(regionDto.getSido());
        regionEntity.setSigungu(regionDto.getSigungu());
        regionEntity.setJibunAddress(regionDto.getJibunAddress());
        regionEntity.setRoadAddress(regionDto.getRoadAddress());
        regionEntity.setBuildingName(regionDto.getBuildingName());
        regionEntity.setAddress(regionDto.getAddress());
        regionEntity.setUserSelectedAddress(regionDto.getUserSelectedAddress());
        regionEntity.setAddressDetail(regionDto.getAddressDetail());
        regionEntity.setFullAddress(FULL_ADDRESS.toString());
        regionEntity.setBname(regionDto.getBname());
        regionEntity.setBname1(regionDto.getBname1());
        regionEntity.setBname2(regionDto.getBname2());
        regionEntity.setUpdatedAt(CustomDateUtils.getCurrentDateTime());
    }
}
