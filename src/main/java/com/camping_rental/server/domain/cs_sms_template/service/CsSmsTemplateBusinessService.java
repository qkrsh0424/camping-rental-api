package com.camping_rental.server.domain.cs_sms_template.service;

import com.camping_rental.server.domain.cs_sms_template.dto.CsSmsTemplateDto;
import com.camping_rental.server.domain.cs_sms_template.entity.CsSmsTemplateEntity;
import com.camping_rental.server.domain.cs_sms_template.vo.CsSmsTemplateVo;
import com.camping_rental.server.domain.enums.DeletedFlagEnums;
import com.camping_rental.server.domain.exception.dto.AccessDeniedPermissionException;
import com.camping_rental.server.domain.room.entity.RoomEntity;
import com.camping_rental.server.domain.room.service.RoomService;
import com.camping_rental.server.domain.user.service.UserService;
import com.camping_rental.server.utils.CustomDateUtils;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CsSmsTemplateBusinessService {
    private final UserService userService;
    private final RoomService roomService;
    private final CsSmsTemplateService csSmsTemplateService;

    @Transactional(readOnly = true)
    public List<CsSmsTemplateVo.Basic> searchList() {
        UUID userId = userService.getUserIdOrThrow();
        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);

        List<CsSmsTemplateEntity> csSmsTemplateEntities = csSmsTemplateService.searchListByRoomId(roomEntity.getId());
        List<CsSmsTemplateVo.Basic> csSmsTemplateVos = csSmsTemplateEntities.stream().map(CsSmsTemplateVo.Basic::toVo).collect(Collectors.toList());

        return csSmsTemplateVos;
    }

    @Transactional(readOnly = true)
    public Page<CsSmsTemplateVo.Basic> searchPage(Pageable pageable) {
        UUID userId = userService.getUserIdOrThrow();
        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);

        Page<CsSmsTemplateEntity> csSmsTemplateEntityPage = csSmsTemplateService.qSearchPageByRoomId(roomEntity.getId(), pageable);
        List<CsSmsTemplateVo.Basic> csSmsTemplateVos = csSmsTemplateEntityPage.getContent().stream().map(CsSmsTemplateVo.Basic::toVo).collect(Collectors.toList());

        return new PageImpl<>(csSmsTemplateVos, pageable, csSmsTemplateEntityPage.getTotalElements());
    }

    @Transactional
    public void createOne(CsSmsTemplateDto.Create csSmsTemplateDto) {
        UUID userId = userService.getUserIdOrThrow();
        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);
        UUID roomId = roomEntity.getId();
        UUID csSmsTemplateId = UUID.randomUUID();

        /**
         * TODO : 템플릿 생성 개수 제한걸기 20개
         */
        CsSmsTemplateEntity csSmsTemplateEntity = CsSmsTemplateEntity.builder()
                .cid(null)
                .id(csSmsTemplateId)
                .name(csSmsTemplateDto.getName())
                .message("")
                .createdAt(CustomDateUtils.getCurrentDateTime())
                .updatedAt(CustomDateUtils.getCurrentDateTime())
                .deletedFlag(DeletedFlagEnums.EXIST.getValue())
                .roomId(roomId)
                .build();

        csSmsTemplateService.saveAndModify(csSmsTemplateEntity);
    }

    @Transactional
    public void changeName(UUID csSmsTemplateId, String name) {
        UUID userId = userService.getUserIdOrThrow();
        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);

        CsSmsTemplateEntity csSmsTemplateEntity = csSmsTemplateService.searchByIdOrThrow(csSmsTemplateId);

        if(!roomEntity.getId().equals(csSmsTemplateEntity.getRoomId())){
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        /*
        Dirty checking update
         */
        csSmsTemplateEntity.setName(name);
    }

    @Transactional
    public void changeMessage(UUID csSmsTemplateId, String message) {
        UUID userId = userService.getUserIdOrThrow();
        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);

        CsSmsTemplateEntity csSmsTemplateEntity = csSmsTemplateService.searchByIdOrThrow(csSmsTemplateId);

        if(!roomEntity.getId().equals(csSmsTemplateEntity.getRoomId())){
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        /*
        Dirty checking update
         */
        csSmsTemplateEntity.setMessage(message);
    }

    @Transactional
    public void deleteOne(UUID csSmsTemplateId) {
        UUID userId = userService.getUserIdOrThrow();
        RoomEntity roomEntity = roomService.searchByUserIdOrThrow(userId);

        CsSmsTemplateEntity csSmsTemplateEntity = csSmsTemplateService.searchByIdOrThrow(csSmsTemplateId);

        if(!roomEntity.getId().equals(csSmsTemplateEntity.getRoomId())){
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        /*
        Dirty checking update
         */
        csSmsTemplateService.logicalDelete(csSmsTemplateEntity);
    }
}
