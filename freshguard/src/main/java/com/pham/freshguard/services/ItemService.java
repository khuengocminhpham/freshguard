package com.pham.freshguard.services;

import com.pham.freshguard.domain.entities.ItemEntity;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    ItemEntity save(ItemEntity itemEntity);
    List<ItemEntity> findAll();
    Optional<ItemEntity> findOne(Long id);
    void deleteAll();
    void delete(Long id);
    boolean isExists(Long id);
    ItemEntity partialUpdate(Long id, ItemEntity itemEntity);

}
