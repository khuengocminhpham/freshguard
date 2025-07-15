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
import org.springframework.test.annotation.DirtiesContext;
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
public class ItemEntityRepositoryIntegrationTests {
    private final ItemRepository underTest;
    private final RecipeRepository recipeRepository;

    @Autowired
    public ItemEntityRepositoryIntegrationTests(ItemRepository underTest, RecipeRepository recipeRepository) {
        this.underTest = underTest;
        this.recipeRepository = recipeRepository;
    }

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }


    @Test
    public void testThatItemCanBeCreatedAndRecalled() {
        ItemEntity itemEntity = TestDataUtil.createTestItemEntityA();
        underTest.save(itemEntity);
        Optional<ItemEntity> result = underTest.findById(itemEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(itemEntity);
    }

    @Test
    public void testThatItemWithRecipesCanBeCreatedAndRecalled() {
        RecipeEntity recipeEntityA = TestDataUtil.createTestRecipeEntityA();
        RecipeEntity recipeEntityB = TestDataUtil.createTestRecipeEntityB();
        recipeRepository.save(recipeEntityA);
        recipeRepository.save(recipeEntityB);

        Set<RecipeEntity> recipes = Stream.of(recipeEntityA, recipeEntityB)
                .collect(Collectors.toSet());

        ItemEntity itemEntity = TestDataUtil.createTestItemEntityA();
        itemEntity.setRecipes(recipes);

        underTest.save(itemEntity);

        Optional<ItemEntity> result = underTest.findById(itemEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getRecipes()).hasSize(2);
        assertThat(result.get().getRecipes()).containsExactlyInAnyOrder(recipeEntityA, recipeEntityB);
    }

    @Test
    public void testThatMultipleItemsCanBeCreatedAndRecalled() {
        ItemEntity itemEntityA = TestDataUtil.createTestItemEntityA();
        underTest.save(itemEntityA);
        ItemEntity itemEntityB = TestDataUtil.createTestItemEntityB();
        underTest.save(itemEntityB);
        ItemEntity itemEntityC = TestDataUtil.createTestItemEntityC();
        underTest.save(itemEntityC);

        Iterable<ItemEntity> result = underTest.findAll();
        assertThat(result)
                .hasSize(3).
                containsExactly(itemEntityA, itemEntityB, itemEntityC);
    }

    @Test
    public void testThatItemCanBeUpdated() {
        ItemEntity itemEntity = TestDataUtil.createTestItemEntityA();
        underTest.save(itemEntity);
        itemEntity.setName("UPDATED");
        underTest.save(itemEntity);
        Optional<ItemEntity> result = underTest.findById(itemEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(itemEntity);
    }

    @Test
    public void testThatItemCanBeDeleted() {
        ItemEntity itemEntity = TestDataUtil.createTestItemEntityA();
        underTest.save(itemEntity);
        underTest.deleteById(itemEntity.getId());
        Optional<ItemEntity> result = underTest.findById(itemEntity.getId());
        assertThat(result).isEmpty();
    }
}
