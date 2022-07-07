package com.camping_rental.server.domain.room.repository;

import com.camping_rental.server.domain.room.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {
    Optional<RoomEntity> findByUserId(UUID userId);

    Optional<RoomEntity> findById(UUID id);
}
