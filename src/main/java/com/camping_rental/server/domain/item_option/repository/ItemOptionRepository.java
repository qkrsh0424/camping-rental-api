package com.camping_rental.server.domain.item_option.repository;

import com.camping_rental.server.domain.item_option.entity.ItemOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ItemOptionRepository extends JpaRepository<ItemOptionEntity, Integer> {
    @Query("SELECT io FROM ItemOptionEntity io WHERE io.itemId IN :itemIds")
    List<ItemOptionEntity> findByItemIds(List<UUID> itemIds);
}
