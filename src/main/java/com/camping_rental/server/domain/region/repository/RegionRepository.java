package com.camping_rental.server.domain.region.repository;

import com.camping_rental.server.domain.region.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegionRepository extends JpaRepository<RegionEntity, Long> {
    List<RegionEntity> findByCreatedBy(UUID createdBy);

    Optional<RegionEntity> findById(UUID id);
}
