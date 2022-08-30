package com.camping_rental.server.domain.room.service;

import com.camping_rental.server.domain.enums.DeletedFlagEnums;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.room.entity.RoomEntity;
import com.camping_rental.server.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    public void saveAndModify(RoomEntity roomEntity){
        roomRepository.save(roomEntity);
    }

    public RoomEntity searchByUserIdOrThrow(UUID userId) {
        return roomRepository.findByUserId(userId).orElseThrow(()-> new NotMatchedFormatException("룸 데이터를 찾을 수 없습니다."));
    }

    public RoomEntity searchByUserIdOrNull(UUID userId) {
        return roomRepository.findByUserId(userId).orElse(null);
    }

    public RoomEntity searchByIdOrThrow(UUID id) {
        return roomRepository.findById(id).orElseThrow(() -> new NotMatchedFormatException("룸 데이터를 찾을 수 없습니다."));
    }

    public RoomEntity searchByPhoneNumber(String phoneNumber) {
        return roomRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }

    public RoomEntity searchByName(String name) {
        return roomRepository.findByName(name).orElse(null);
    }

    public void logicalDelete(RoomEntity roomEntity) {
        roomEntity.setDeletedFlag(DeletedFlagEnums.DELETED.getValue());
    }
}
