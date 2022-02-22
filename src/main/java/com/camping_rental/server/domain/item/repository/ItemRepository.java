package com.camping_rental.server.domain.item.repository;

import com.camping_rental.server.domain.item.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {
    List<ItemEntity> findByCategoryId(UUID categoryId);

    List<ItemEntity> findById(UUID itemId);

    @Query("SELECT i FROM ItemEntity i WHERE i.displayYn='y'")
    List<ItemEntity> findDisplayList();

    @Query("SELECT i FROM ItemEntity i WHERE i.displayYn='y' AND i.categoryId=:categoryId")
    List<ItemEntity> findDisplayListByCategoryId(UUID categoryId);
}
