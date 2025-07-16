package com.pham.freshguard.controllers;

import com.pham.freshguard.domain.dto.ItemDto;
import com.pham.freshguard.domain.entities.ItemEntity;
import com.pham.freshguard.mappers.Mapper;
import com.pham.freshguard.services.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ItemController {
    private final ItemService itemService;
    private final Mapper<ItemEntity, ItemDto> itemMapper;
    public ItemController(ItemService itemService, Mapper<ItemEntity, ItemDto> itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @PostMapping(path = "/items")
    public ResponseEntity<ItemDto> createItem (@RequestBody ItemDto item) {
        ItemEntity itemEntity = itemMapper.mapFrom(item);
        ItemEntity savedItemEntity =  itemService.save(itemEntity);
        return new ResponseEntity<>(itemMapper.mapTo(savedItemEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/items")
    public List<ItemDto> getItems() {
        List<ItemEntity> items = itemService.findAll();
        return items.stream()
                .map(itemMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/items/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable("id") Long id) {
        Optional<ItemEntity> item = itemService.findOne(id);
        return item.map(itemEntity -> {
            ItemDto itemDto = itemMapper.mapTo(itemEntity);
            return new ResponseEntity<>(itemDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/items/{id}")
    public ResponseEntity deleteItem(@PathVariable("id") Long id) {
        itemService.delete(id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
