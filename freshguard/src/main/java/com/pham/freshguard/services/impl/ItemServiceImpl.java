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
    public void deleteAll() {
        itemRepository.deleteAll();
    }
}
