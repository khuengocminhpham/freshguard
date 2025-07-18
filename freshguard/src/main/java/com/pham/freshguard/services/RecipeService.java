package com.pham.freshguard.services;

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
}
