package com.pham.freshguard.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pham.freshguard.TestDataUtil;
import com.pham.freshguard.domain.entities.ItemEntity;
import com.pham.freshguard.services.ItemService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@Rollback
@AutoConfigureMockMvc
public class ItemControllerIntegrationTests {
    private final ItemService itemService;
    private final MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public ItemControllerIntegrationTests(MockMvc mockMvc, ItemService itemService) {
        this.itemService = itemService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @BeforeEach()
    void setUp() {
        itemService.deleteAll();
    }

    @Test
    public void testThatCreateItemSuccessfullyReturnsHttp201Created() throws Exception {
        ItemEntity itemEntity = TestDataUtil.createTestItemEntityA();
        String authorJson = objectMapper.writeValueAsString(itemEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateItemSuccessfullyReturnsSavedAuthor() throws Exception {
        ItemEntity itemEntity = TestDataUtil.createTestItemEntityA();
        String authorJson = objectMapper.writeValueAsString(itemEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
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
                MockMvcRequestBuilders.get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetItemsReturnsListOfItems() throws Exception {
        ItemEntity itemEntityA = TestDataUtil.createTestItemEntityA();
        itemService.save(itemEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/items")
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
                MockMvcRequestBuilders.get("/items/" + savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
