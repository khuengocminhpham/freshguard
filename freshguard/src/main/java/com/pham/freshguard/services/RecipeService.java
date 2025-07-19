package com.pham.freshguard.services;

import com.pham.freshguard.domain.entities.ItemEntity;
import com.pham.freshguard.domain.entities.RecipeEntity;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    RecipeEntity save(RecipeEntity recipeEntity);
    List<RecipeEntity> findAll();
    Optional<RecipeEntity> findOne(Long id);
    void deleteAll();
    void delete(Long id);
    boolean isExists(Long id);
    RecipeEntity partialUpdate(Long id, RecipeEntity recipeEntity);
    RecipeEntity addIngredientToRecipe(Long recipeId, Long itemId);
    RecipeEntity removeIngredientFromRecipe(Long recipeId, Long itemId);
    List<ItemEntity> getRecipeIngredients(Long recipeId);
    List<RecipeEntity> findRecipesContainingIngredients(List<Long> itemIds);
    RecipeEntity setRecipeIngredients(Long recipeId, List<Long> itemIds);

}
