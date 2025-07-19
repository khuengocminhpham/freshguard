package com.pham.freshguard.services.impl;

import com.pham.freshguard.domain.entities.ItemEntity;
import com.pham.freshguard.domain.entities.RecipeEntity;
import com.pham.freshguard.repositories.ItemRepository;
import com.pham.freshguard.repositories.RecipeRepository;
import com.pham.freshguard.services.RecipeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RecipeServiceImpl implements RecipeService {
    private RecipeRepository recipeRepository;
    private ItemRepository itemRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository, ItemRepository itemRepository) {
        this.recipeRepository = recipeRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public RecipeEntity save(RecipeEntity recipeEntity) {
        return recipeRepository.save(recipeEntity);
    }

    @Override
    public List<RecipeEntity> findAll() {
        return StreamSupport.stream(recipeRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RecipeEntity> findOne(Long id) {
        return recipeRepository.findById(id);
    }

    @Override
    public void deleteAll() {
        recipeRepository.deleteAll();
    }

    @Override
    public void delete(Long id) {
        recipeRepository.deleteById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return recipeRepository.existsById(id);
    }

    @Override
    public RecipeEntity partialUpdate(Long id, RecipeEntity recipeEntity) {
        recipeEntity.setId(id);

        return recipeRepository.findById(id).map(recipe -> {
            Optional.ofNullable(recipeEntity.getName()).ifPresent(recipe::setName);
            Optional.ofNullable(recipeEntity.getDescription()).ifPresent(recipe::setDescription);
            Optional.ofNullable(recipeEntity.getInstructions()).ifPresent(recipe::setInstructions);
            Optional.ofNullable(recipeEntity.getServings()).ifPresent(recipe::setServings);
            Optional.ofNullable(recipeEntity.getPrepTimeMinutes()).ifPresent(recipe::setPrepTimeMinutes);
            Optional.ofNullable(recipeEntity.getIngredients()).ifPresent(recipe::setIngredients);
            return recipeRepository.save(recipe);
        }).orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    @Override
    public RecipeEntity addIngredientToRecipe(Long recipeId, Long itemId) {
        RecipeEntity recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        recipe.getIngredients().add(item);
        return recipeRepository.save(recipe);
    }

    @Override
    public RecipeEntity removeIngredientFromRecipe(Long recipeId, Long itemId) {
        RecipeEntity recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        recipe.getIngredients().removeIf(item -> item.getId().equals(itemId));
        return recipeRepository.save(recipe);
    }

    @Override
    public List<ItemEntity> getRecipeIngredients(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .map(recipe -> recipe.getIngredients().stream().collect(Collectors.toList()))
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    @Override
    public List<RecipeEntity> findRecipesContainingIngredients(List<Long> itemIds) {
        return recipeRepository.findRecipesContainingIngredients(itemIds);
    }

    @Override
    public RecipeEntity setRecipeIngredients(Long recipeId, List<Long> itemIds) {
        RecipeEntity recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        Set<ItemEntity> newIngredients = itemIds.stream()
                .map(itemId -> itemRepository.findById(itemId)
                        .orElseThrow(() -> new RuntimeException("Item not found: " + itemId)))
                .collect(Collectors.toSet());

        recipe.setIngredients(newIngredients);
        return recipeRepository.save(recipe);
    }


}
