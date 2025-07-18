package com.pham.freshguard.services.impl;

import com.pham.freshguard.domain.entities.ItemEntity;
import com.pham.freshguard.repositories.ItemRepository;
import com.pham.freshguard.services.ItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemEntity save(ItemEntity itemEntity) {
        return itemRepository.save(itemEntity);
    }

    @Override
    public List<ItemEntity> findAll() {
        return StreamSupport.stream(itemRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ItemEntity> findOne(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public void delete(Long id){
        itemRepository.deleteById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return itemRepository.existsById(id);
    }

    @Override
    public ItemEntity partialUpdate(Long id, ItemEntity itemEntity) {
        itemEntity.setId(id);

        return itemRepository.findById(id).map(item -> {
            Optional.ofNullable(itemEntity.getName()).ifPresent(item::setName);
            Optional.ofNullable(itemEntity.getCategory()).ifPresent(item::setCategory);
            Optional.ofNullable(itemEntity.getExpirationDate()).ifPresent(item::setExpirationDate);
            Optional.ofNullable(itemEntity.getPurchaseDate()).ifPresent(item::setPurchaseDate);
            Optional.ofNullable(itemEntity.getQuantity()).ifPresent(item::setQuantity);
            Optional.ofNullable(itemEntity.getLocation()).ifPresent(item::setLocation);
            Optional.ofNullable(itemEntity.getRecipes()).ifPresent(item::setRecipes);
            return itemRepository.save(item);
        }).orElseThrow(() -> new RuntimeException("Item does not exist"));
    }

    @Override
    public void deleteAll() {
        itemRepository.deleteAll();
    }
}
