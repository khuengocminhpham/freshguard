package com.pham.freshguard.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pham.freshguard.TestDataUtil;
import com.pham.freshguard.domain.entities.RecipeEntity;
import com.pham.freshguard.services.RecipeService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@Rollback
@AutoConfigureMockMvc
public class RecipeControllerIntegrationTests {
    private final RecipeService recipeService;
    private final MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public RecipeControllerIntegrationTests(MockMvc mockMvc, RecipeService recipeService) {
        this.recipeService = recipeService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @BeforeEach()
    void setUp() {
        recipeService.deleteAll();
    }

    @Test
    public void testThatCreateRecipeSuccessfullyReturnsHttp201Created() throws Exception {
        RecipeEntity recipeEntity = TestDataUtil.createTestRecipeEntityA();
        String recipeJson = objectMapper.writeValueAsString(recipeEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(recipeJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateRecipeSuccessfullyReturnsSavedRecipe() throws Exception {
        RecipeEntity recipeEntity = TestDataUtil.createTestRecipeEntityA();
        String recipeJson = objectMapper.writeValueAsString(recipeEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(recipeJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Soup")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value("Traditional chicken soup")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.instructions").value("Boil water then add seasonings")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.servings").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.prepTimeMinutes").value(20)
        );
    }

    @Test
    public void testThatGetRecipesReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetRecipesReturnsListOfRecipes() throws Exception {
        RecipeEntity recipeEntity = TestDataUtil.createTestRecipeEntityA();
        recipeService.save(recipeEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Soup")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].description").value("Traditional chicken soup")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].instructions").value("Boil water then add seasonings")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].servings").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].prepTimeMinutes").value(20)
        );
    }

    @Test
    public void testThatGetRecipeReturnsHttpStatus200WhenRecipeExist() throws Exception {
        RecipeEntity recipeEntity = TestDataUtil.createTestRecipeEntityA();
        RecipeEntity savedRecipe = recipeService.save(recipeEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/recipes/" + savedRecipe.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetRecipeReturnsRecipeWhenRecipeExist() throws Exception {
        RecipeEntity recipeEntity = TestDataUtil.createTestRecipeEntityA();
        RecipeEntity savedRecipe = recipeService.save(recipeEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/recipes/" + savedRecipe.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedRecipe.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Soup")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value("Traditional chicken soup")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.instructions").value("Boil water then add seasonings")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.servings").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.prepTimeMinutes").value(20)
        );
    }

    @Test
    public void testThatGetRecipeReturnsHttpStatus404WhenNoRecipeExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/recipes/9999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateRecipeReturnsHttpStatus404WhenNoRecipeExists() throws Exception {
        RecipeEntity recipe = TestDataUtil.createTestRecipeEntityA();
        String recipeJson = objectMapper.writeValueAsString(recipe);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/recipes/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(recipeJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateRecipeReturnsHttpStatus200WhenRecipeExists() throws Exception {
        RecipeEntity recipe = TestDataUtil.createTestRecipeEntityA();
        RecipeEntity savedRecipe = recipeService.save(recipe);

        String recipeJson = objectMapper.writeValueAsString(recipe);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/recipes/" + savedRecipe.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(recipeJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateUpdatesExistingRecipe() throws Exception {
        RecipeEntity recipeEntityA = TestDataUtil.createTestRecipeEntityA();
        RecipeEntity savedRecipe = recipeService.save(recipeEntityA);

        RecipeEntity recipeEntityB = TestDataUtil.createTestRecipeEntityB();
        recipeEntityB.setId(savedRecipe.getId());

        String recipeUpdatedJson = objectMapper.writeValueAsString(recipeEntityB);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/recipes/" + savedRecipe.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(recipeUpdatedJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedRecipe.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(recipeEntityB.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(recipeEntityB.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.instructions").value(recipeEntityB.getInstructions())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.servings").value(recipeEntityB.getServings())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.prepTimeMinutes").value(recipeEntityB.getPrepTimeMinutes())
        );
    }

    @Test
    public void testThatPartialUpdateExistingRecipeReturnsHttpStatus20Ok() throws Exception {
        RecipeEntity recipe = TestDataUtil.createTestRecipeEntityA();
        RecipeEntity savedRecipe = recipeService.save(recipe);

        recipe.setName("UPDATED");
        String recipeJson = objectMapper.writeValueAsString(recipe);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/recipes/" + savedRecipe.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(recipeJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatPartialUpdateExistingRecipeReturnsUpdatedRecipe() throws Exception {
        RecipeEntity recipe = TestDataUtil.createTestRecipeEntityA();
        RecipeEntity savedRecipe = recipeService.save(recipe);

        RecipeEntity newRecipe = new RecipeEntity();
        newRecipe.setName("UPDATED");
        String recipeJson = objectMapper.writeValueAsString(newRecipe);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/recipes/" + savedRecipe.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(recipeJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedRecipe.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("UPDATED")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(savedRecipe.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.instructions").value(savedRecipe.getInstructions())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.servings").value(savedRecipe.getServings())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.prepTimeMinutes").value(savedRecipe.getPrepTimeMinutes())
        );
    }

    @Test
    public void testThatDeleteRecipeReturnsHttpStatus204ForNonExistingRecipe() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/recipes/9999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteRecipeReturnsHttpStatus204ForExistingRecipe() throws Exception {
        RecipeEntity recipe = TestDataUtil.createTestRecipeEntityA();
        RecipeEntity savedRecipe = recipeService.save(recipe);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/recipes/" + savedRecipe.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
