package com.pham.freshguard.controllers;

import com.pham.freshguard.domain.dto.RecipeDto;
import com.pham.freshguard.domain.entities.RecipeEntity;
import com.pham.freshguard.mappers.Mapper;
import com.pham.freshguard.services.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private RecipeService recipeService;
    private Mapper<RecipeEntity, RecipeDto> recipeMapper;

    public RecipeController(RecipeService recipeService, Mapper<RecipeEntity, RecipeDto> recipeMapper) {
        this.recipeService = recipeService;
        this.recipeMapper = recipeMapper;
    }

    @PostMapping()
    public ResponseEntity<RecipeDto> createRecipe (@RequestBody RecipeDto recipe) {
        RecipeEntity recipeEntity = recipeMapper.mapFrom(recipe);
        RecipeEntity savedRecipeEntity=  recipeService.save(recipeEntity);
        return new ResponseEntity<>(recipeMapper.mapTo(savedRecipeEntity), HttpStatus.CREATED);
    }

    @GetMapping()
    public List<RecipeDto> getRecipes() {
        List<RecipeEntity> recipes = recipeService.findAll();
        return recipes.stream()
                .map(recipeMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable("id") Long id) {
        Optional<RecipeEntity> recipe = recipeService.findOne(id);
        return recipe.map(recipeEntity -> {
            RecipeDto recipeDto = recipeMapper.mapTo(recipeEntity);
            return new ResponseEntity<>(recipeDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<RecipeDto> fullUpdateRecipe(
            @PathVariable("id") Long id,
            @RequestBody RecipeDto recipeDto) {

        if(!recipeService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        recipeDto.setId(id);
        RecipeEntity recipeEntity = recipeMapper.mapFrom(recipeDto);
        RecipeEntity savedRecipe = recipeService.save(recipeEntity);
        return new ResponseEntity<>(
                recipeMapper.mapTo(savedRecipe),
                HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<RecipeDto> partialUpdateRecipe(
            @PathVariable("id") Long id,
            @RequestBody RecipeDto recipeDto
    ) {
        if(!recipeService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        RecipeEntity recipeEntity = recipeMapper.mapFrom(recipeDto);
        RecipeEntity updatedRecipe = recipeService.partialUpdate(id, recipeEntity);
        return new ResponseEntity<>(
                recipeMapper.mapTo(updatedRecipe),
                HttpStatus.OK);
    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteRecipe(@PathVariable("id") Long id) {
        recipeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{recipeId}/ingredients/{itemId}")
    public ResponseEntity<RecipeDto> addIngredientToRecipe(
            @PathVariable Long recipeId,
            @PathVariable Long itemId) {
        try {
            RecipeEntity recipeEntity = recipeService.addIngredientToRecipe(recipeId, itemId);
            return new ResponseEntity<>(recipeMapper.mapTo(recipeEntity), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{recipeId}/ingredients/{itemId}")
    public ResponseEntity<RecipeDto> removeIngredientFromRecipe(
            @PathVariable Long recipeId,
            @PathVariable Long itemId) {
        try {
            RecipeEntity recipeEntity = recipeService.removeIngredientFromRecipe(recipeId, itemId);
            return new ResponseEntity<>(recipeMapper.mapTo(recipeEntity), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{recipeId}/ingredients")
    public ResponseEntity<RecipeDto> setRecipeIngredients(
            @PathVariable Long recipeId,
            @RequestBody List<Long> itemIds) {
        try {
            RecipeEntity recipeEntity = recipeService.setRecipeIngredients(recipeId, itemIds);
            return new ResponseEntity<>(recipeMapper.mapTo(recipeEntity), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/find-by-ingredients")
    public ResponseEntity<List<RecipeDto>> findRecipesContainingAnyIngredients(
            @RequestBody List<Long> itemIds) {
        List<RecipeEntity> recipes = recipeService.findRecipesContainingIngredients(itemIds);
        List<RecipeDto> recipesDto = recipes.stream()
                .map(recipeMapper::mapTo)
                .collect(Collectors.toList());
        return new ResponseEntity<>(recipesDto, HttpStatus.OK);
    }
}
