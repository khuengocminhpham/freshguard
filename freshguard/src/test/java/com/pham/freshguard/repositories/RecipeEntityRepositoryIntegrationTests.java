package com.pham.freshguard.repositories;

import com.pham.freshguard.TestDataUtil;
import com.pham.freshguard.domain.entities.ItemEntity;
import com.pham.freshguard.domain.entities.RecipeEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@Rollback
public class RecipeEntityRepositoryIntegrationTests {
    private final RecipeRepository underTest;
    private final ItemRepository itemRepository;

    @Autowired
    public RecipeEntityRepositoryIntegrationTests(RecipeRepository underTest, ItemRepository itemRepository) {
        this.underTest = underTest;
        this.itemRepository = itemRepository;
    }

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    public void testThatRecipeCanBeCreatedAndRecalled() {
//        ItemEntity itemEntityA = TestDataUtil.createTestItemEntityA();
//        ItemEntity itemEntityB = TestDataUtil.createTestItemEntityB();
//        itemRepository.save(itemEntityA);
//        itemRepository.save(itemEntityB);
//        Set<ItemEntity> ingredients = Stream.of(itemEntityA, itemEntityB).collect(Collectors.toSet());

        RecipeEntity recipeEntity = TestDataUtil.createTestRecipeEntityA();
        underTest.save(recipeEntity);
        Optional<RecipeEntity> result = underTest.findById(recipeEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(recipeEntity);
    }

    @Test
    public void testThatRecipeWithIngredientsCanBeCreatedAndRecalled() {
        ItemEntity itemEntityA = TestDataUtil.createTestItemEntityA();
        ItemEntity itemEntityB = TestDataUtil.createTestItemEntityB();
        itemRepository.save(itemEntityA);
        itemRepository.save(itemEntityB);

        Set<ItemEntity> ingredients = Stream.of(itemEntityA, itemEntityB)
                .collect(Collectors.toSet());

        RecipeEntity recipeEntity = TestDataUtil.createTestRecipeEntityA();
        recipeEntity.setIngredients(ingredients);

        underTest.save(recipeEntity);

        Optional<RecipeEntity> result = underTest.findById(recipeEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getIngredients()).hasSize(2);
        assertThat(result.get().getIngredients()).containsExactlyInAnyOrder(itemEntityA, itemEntityB);
    }

    @Test
    public void testThatMultipleRecipesCanBeCreatedAndRecalled() {
        RecipeEntity recipeEntityA = TestDataUtil.createTestRecipeEntityA();
        underTest.save(recipeEntityA);
        RecipeEntity recipeEntityB = TestDataUtil.createTestRecipeEntityB();
        underTest.save(recipeEntityB);
        RecipeEntity recipeEntityC = TestDataUtil.createTestRecipeEntityC();
        underTest.save(recipeEntityC);

        Iterable<RecipeEntity> result = underTest.findAll();
        assertThat(result)
                .hasSize(3).
                containsExactly(recipeEntityA, recipeEntityB, recipeEntityC);
    }

    @Test
    public void testThatRecipeCanBeUpdated() {
        RecipeEntity recipeEntity = TestDataUtil.createTestRecipeEntityA();
        underTest.save(recipeEntity);
        recipeEntity.setName("UPDATED");
        underTest.save(recipeEntity);
        Optional<RecipeEntity> result = underTest.findById(recipeEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(recipeEntity);
    }

    @Test
    public void testThatRecipeCanBeDeleted() {
        RecipeEntity recipeEntity = TestDataUtil.createTestRecipeEntityA();
        underTest.save(recipeEntity);
        underTest.deleteById(recipeEntity.getId());
        Optional<RecipeEntity> result = underTest.findById(recipeEntity.getId());
        assertThat(result).isEmpty();
    }
}
