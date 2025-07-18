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
public class RecipeController {
    private RecipeService recipeService;
    private Mapper<RecipeEntity, RecipeDto> recipeMapper;

    public RecipeController(RecipeService recipeService, Mapper<RecipeEntity, RecipeDto> recipeMapper) {
        this.recipeService = recipeService;
        this.recipeMapper = recipeMapper;
    }

    @PostMapping(path = "/recipes")
    public ResponseEntity<RecipeDto> createRecipe (@RequestBody RecipeDto recipe) {
        RecipeEntity recipeEntity = recipeMapper.mapFrom(recipe);
        RecipeEntity savedRecipeEntity=  recipeService.save(recipeEntity);
        return new ResponseEntity<>(recipeMapper.mapTo(savedRecipeEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/recipes")
    public List<RecipeDto> getRecipes() {
        List<RecipeEntity> recipes = recipeService.findAll();
        return recipes.stream()
                .map(recipeMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/recipes/{id}")
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable("id") Long id) {
        Optional<RecipeEntity> recipe = recipeService.findOne(id);
        return recipe.map(recipeEntity -> {
            RecipeDto recipeDto = recipeMapper.mapTo(recipeEntity);
            return new ResponseEntity<>(recipeDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/recipes/{id}")
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

    @PatchMapping(path = "/recipes/{id}")
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


    @DeleteMapping(path = "/recipes/{id}")
    public ResponseEntity deleteRecipe(@PathVariable("id") Long id) {
        recipeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
