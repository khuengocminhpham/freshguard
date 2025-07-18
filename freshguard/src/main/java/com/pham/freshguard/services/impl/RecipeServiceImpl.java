package com.pham.freshguard.services.impl;

import com.pham.freshguard.domain.entities.RecipeEntity;
import com.pham.freshguard.repositories.RecipeRepository;
import com.pham.freshguard.services.RecipeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RecipeServiceImpl implements RecipeService {
    private RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
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
        }).orElseThrow(() -> new RuntimeException("Recipe does not exist"));
    }
}
