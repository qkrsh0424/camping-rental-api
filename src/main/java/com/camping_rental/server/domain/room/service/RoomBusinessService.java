package com.camping_rental.server.domain.room.service;

import com.camping_rental.server.domain.exception.dto.AccessDeniedPermissionException;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.room.entity.RoomEntity;
import com.camping_rental.server.domain.room.vo.RoomVo;
import com.camping_rental.server.domain.user.service.UserService;
import com.camping_rental.server.utils.CustomDateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomBusinessService {
    private final UserService userService;
    private final RoomSerivce roomSerivce;

    @Transactional(readOnly = true)
    public Object searchOne(UUID id){
        RoomEntity roomEntity = roomSerivce.searchByIdOrThrow(id);

        RoomVo.Basic roomVo = RoomVo.Basic.toVo(roomEntity);
        return roomVo;
    }

    @Transactional
    public void changeIntroduction(UUID roomId, String introduction) {
        UUID USER_ID = userService.getUserIdOrThrow();
        RoomEntity roomEntity = roomSerivce.searchByIdOrThrow(roomId);

        if(introduction.length() > 400){
            throw new NotMatchedFormatException("소개말은 최대 400자 까지 작성할 수 있습니다.");
        }

        if(!roomEntity.getUserId().equals(USER_ID)){
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        /*
        Dirty Check Update
         */
        roomEntity.setIntroduction(introduction);
        roomEntity.setUpdatedAt(CustomDateUtils.getCurrentDateTime());
    }
}
