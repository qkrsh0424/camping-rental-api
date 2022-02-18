package com.camping_rental.server.domain.item.repository;

import com.camping_rental.server.domain.item.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {
    List<ItemEntity> findByCategoryId(UUID category_id);
}
