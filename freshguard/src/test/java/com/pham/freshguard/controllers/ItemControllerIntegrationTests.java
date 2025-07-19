package com.pham.freshguard.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pham.freshguard.TestDataUtil;
import com.pham.freshguard.domain.entities.ItemEntity;
import com.pham.freshguard.services.ItemService;
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
public class ItemControllerIntegrationTests {
    private final ItemService itemService;
    private final RecipeService recipeService;
    private final MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public ItemControllerIntegrationTests(MockMvc mockMvc, ItemService itemService, RecipeService recipeService) {
        this.itemService = itemService;
        this.recipeService = recipeService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @BeforeEach()
    void setUp() {
        recipeService.deleteAll();
        itemService.deleteAll();
    }

    @Test
    public void testThatCreateItemSuccessfullyReturnsHttp201Created() throws Exception {
        ItemEntity itemEntity = TestDataUtil.createTestItemEntityA();
        String itemJson = objectMapper.writeValueAsString(itemEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateItemSuccessfullyReturnsSavedItem() throws Exception {
        ItemEntity itemEntity = TestDataUtil.createTestItemEntityA();
        String itemJson = objectMapper.writeValueAsString(itemEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Salt")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.category").value("Spices")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.expirationDate").value("2026-01-16")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.purchaseDate").value("2025-07-16")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.location").value("Pantry")
        );
    }

    @Test
    public void testThatGetItemsReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetItemsReturnsListOfItems() throws Exception {
        ItemEntity itemEntityA = TestDataUtil.createTestItemEntityA();
        itemService.save(itemEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Salt")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].category").value("Spices")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].expirationDate").value("2026-01-16")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].purchaseDate").value("2025-07-16")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].quantity").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].location").value("Pantry")
        );
    }

    @Test
    public void testThatGetItemReturnsHttpStatus200WhenItemExist() throws Exception {
        ItemEntity itemEntity = TestDataUtil.createTestItemEntityA();
        ItemEntity savedItem = itemService.save(itemEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/items/" + savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetItemReturnsItemWhenItemExist() throws Exception {
        ItemEntity itemEntity = TestDataUtil.createTestItemEntityA();
        ItemEntity savedItem = itemService.save(itemEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/items/" + savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedItem.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Salt")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.category").value("Spices")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.expirationDate").value("2026-01-16")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.purchaseDate").value("2025-07-16")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.location").value("Pantry")
        );
    }

    @Test
    public void testThatGetItemReturnsHttpStatus404WhenNoItemExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/items/9999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateItemReturnsHttpStatus404WhenNoItemExists() throws Exception {
        ItemEntity item = TestDataUtil.createTestItemEntityA();
        String itemJson = objectMapper.writeValueAsString(item);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/items/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateItemReturnsHttpStatus200WhenItemExists() throws Exception {
        ItemEntity item = TestDataUtil.createTestItemEntityA();
        ItemEntity savedItem = itemService.save(item);

        String itemJson = objectMapper.writeValueAsString(item);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/items/" + savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateUpdatesExistingItem() throws Exception {
        ItemEntity itemEntityA = TestDataUtil.createTestItemEntityA();
        ItemEntity savedItem = itemService.save(itemEntityA);

        ItemEntity itemEntityB = TestDataUtil.createTestItemEntityB();
        itemEntityB.setId(savedItem.getId());

        String itemUpdatedJson = objectMapper.writeValueAsString(itemEntityB);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/items/" + savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemUpdatedJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedItem.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(itemEntityB.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.category").value(itemEntityB.getCategory())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.expirationDate").value("2025-07-22")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.purchaseDate").value("2025-07-16")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity").value(itemEntityB.getQuantity())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.location").value(itemEntityB.getLocation())
        );
    }

    @Test
    public void testThatPartialUpdateExistingItemReturnsHttpStatus20Ok() throws Exception {
        ItemEntity item = TestDataUtil.createTestItemEntityA();
        ItemEntity savedItem = itemService.save(item);

        item.setName("UPDATED");
        String itemJson = objectMapper.writeValueAsString(item);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/items/" + savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatPartialUpdateExistingItemReturnsUpdatedItem() throws Exception {
        ItemEntity item = TestDataUtil.createTestItemEntityA();
        ItemEntity savedItem = itemService.save(item);

        ItemEntity newItem = new ItemEntity();
        newItem.setName("UPDATED");
        String itemJson = objectMapper.writeValueAsString(newItem);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/items/" + savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedItem.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("UPDATED")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.category").value(savedItem.getCategory())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.expirationDate").value("2026-01-16")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.purchaseDate").value("2025-07-16")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity").value(savedItem.getQuantity())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.location").value(savedItem.getLocation())
        );
    }

    @Test
    public void testThatDeleteItemReturnsHttpStatus204ForNonExistingItem() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/items/9999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteItemReturnsHttpStatus204ForExistingItem() throws Exception {
        ItemEntity item = TestDataUtil.createTestItemEntityA();
        ItemEntity savedItem = itemService.save(item);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/items/" + savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
