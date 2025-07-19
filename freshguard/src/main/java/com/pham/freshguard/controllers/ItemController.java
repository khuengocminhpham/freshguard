package com.pham.freshguard.controllers;

import com.pham.freshguard.domain.dto.ItemDto;
import com.pham.freshguard.domain.dto.RecipeDto;
import com.pham.freshguard.domain.entities.ItemEntity;
import com.pham.freshguard.domain.entities.RecipeEntity;
import com.pham.freshguard.mappers.Mapper;
import com.pham.freshguard.services.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;
    private final Mapper<ItemEntity, ItemDto> itemMapper;
    private final Mapper<RecipeEntity, RecipeDto> recipeMapper;
    public ItemController(ItemService itemService, Mapper<ItemEntity, ItemDto> itemMapper, Mapper<RecipeEntity, RecipeDto> recipeMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
        this.recipeMapper = recipeMapper;
    }

    @PostMapping()
    public ResponseEntity<ItemDto> createItem (@RequestBody ItemDto item) {
        ItemEntity itemEntity = itemMapper.mapFrom(item);
        ItemEntity savedItemEntity =  itemService.save(itemEntity);
        return new ResponseEntity<>(itemMapper.mapTo(savedItemEntity), HttpStatus.CREATED);
    }

    @GetMapping()
    public List<ItemDto> getItems() {
        List<ItemEntity> items = itemService.findAll();
        return items.stream()
                .map(itemMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable("id") Long id) {
        Optional<ItemEntity> item = itemService.findOne(id);
        return item.map(itemEntity -> {
            ItemDto itemDto = itemMapper.mapTo(itemEntity);
            return new ResponseEntity<>(itemDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ItemDto> fullUpdateItem(
            @PathVariable("id") Long id,
            @RequestBody ItemDto itemDto) {

        if(!itemService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        itemDto.setId(id);
        ItemEntity itemEntity = itemMapper.mapFrom(itemDto);
        ItemEntity savedItem = itemService.save(itemEntity);
        return new ResponseEntity<>(
                itemMapper.mapTo(savedItem),
                HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<ItemDto> partialUpdateItem(
            @PathVariable("id") Long id,
            @RequestBody ItemDto itemDto
    ) {
        if(!itemService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ItemEntity itemEntity = itemMapper.mapFrom(itemDto);
        ItemEntity updatedItem = itemService.partialUpdate(id, itemEntity);
        return new ResponseEntity<>(
                itemMapper.mapTo(updatedItem),
                HttpStatus.OK);
    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteItem(@PathVariable("id") Long id) {
        itemService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/recipes")
    public ResponseEntity<List<RecipeDto>> getItemRecipes(@PathVariable Long id) {
        Optional<ItemEntity> item = itemService.findOne(id);
        return item.map(itemEntity -> {
            List<RecipeEntity> recipes = new ArrayList<>(itemEntity.getRecipes());
            List<RecipeDto> recipesDto = recipes.stream()
                    .map(recipeMapper::mapTo)
                    .collect(Collectors.toList());
            return  new ResponseEntity<>(recipesDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
}
