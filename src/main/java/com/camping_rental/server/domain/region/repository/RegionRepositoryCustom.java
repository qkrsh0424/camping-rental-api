package com.camping_rental.server.domain.region.repository;

import com.camping_rental.server.domain.region.entity.RegionEntity;
import com.camping_rental.server.domain.region.projection.RegionProjection;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RegionRepositoryCustom {
    List<RegionEntity> qSelectByUserId(UUID userId);

    List<RegionProjection.SidoAndSigungus> qSelectListSidoAndSigungus();
}
