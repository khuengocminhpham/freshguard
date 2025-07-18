package com.pham.freshguard;

import com.pham.freshguard.domain.entities.ItemEntity;
import com.pham.freshguard.domain.entities.RecipeEntity;
import org.assertj.core.util.Sets;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDataUtil {
    private TestDataUtil() {}
    public static ItemEntity createTestItemEntityA() {

        return ItemEntity.builder()
                .name("Salt")
                .category("Spices")
                .expirationDate(LocalDate.of(2025, 7, 16).plusMonths(6)) // example expiration
                .purchaseDate(LocalDate.of(2025, 7, 16))
                .quantity(1)
                .location("Pantry")
                .build();
    }

    public static ItemEntity createTestItemEntityB() {

        return ItemEntity.builder()
                .name("Pork")
                .category("Meat")
                .expirationDate(LocalDate.of(2025, 7, 16).plusDays(6)) // example expiration
                .purchaseDate(LocalDate.of(2025, 7, 16))
                .quantity(2)
                .location("Pantry")
                .build();
    }

    public static ItemEntity createTestItemEntityC() {

        return ItemEntity.builder()
                .name("Milk")
                .category("Diary")
                .expirationDate(LocalDate.now().plusDays(6)) // example expiration
                .purchaseDate(LocalDate.now())
                .quantity(1)
                .location("Fridge")
                .build();
    }

    public static RecipeEntity createTestRecipeEntityA() {
        return RecipeEntity.builder()
                .name("Soup")
                .description("Traditional chicken soup")
                .instructions("Boil water then add seasonings")
                .servings(1)
                .prepTimeMinutes(20)
                .createdAt(LocalDateTime.now())
                .build();
    }
    public static RecipeEntity createTestRecipeEntityB() {
        return RecipeEntity.builder()
                .name("Pasta")
                .description("Creamy garlic Alfredo pasta")
                .instructions("Boil pasta, make sauce, combine")
                .servings(2)
                .prepTimeMinutes(30)
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();
    }

    public static RecipeEntity createTestRecipeEntityC() {
        return RecipeEntity.builder()
                .name("Salad")
                .description("Fresh garden salad with vinaigrette")
                .instructions("Chop veggies, mix, and dress")
                .servings(4)
                .prepTimeMinutes(10)
                .createdAt(LocalDateTime.now().minusDays(2))
                .build();
    }
}
