package com.camping_rental.server.domain.handling_area.repository;

import com.camping_rental.server.domain.handling_area.entity.HandlingAreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandlingAreaRepository extends JpaRepository<HandlingAreaEntity, Integer> {
}
