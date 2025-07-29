package com.pham.freshguard.services.impl;

import com.pham.freshguard.domain.entities.ItemEntity;
import com.pham.freshguard.domain.entities.RecipeEntity;
import com.pham.freshguard.repositories.ItemRepository;
import com.pham.freshguard.repositories.RecipeRepository;
import com.pham.freshguard.services.ItemService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;
    private RecipeRepository recipeRepository;
    public ItemServiceImpl(ItemRepository itemRepository, RecipeRepository recipeRepository) {
        this.itemRepository = itemRepository;
        this.recipeRepository = recipeRepository;
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
        List<Long> itemIds = new ArrayList<>();
        itemIds.add(id);
        List<RecipeEntity> recipesUsingItem = recipeRepository.findRecipesContainingIngredients(itemIds);

        for (RecipeEntity recipe : recipesUsingItem) {
            recipe.getIngredients().removeIf(ingredient -> ingredient.getId().equals(id));
            recipeRepository.save(recipe);
        }
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
