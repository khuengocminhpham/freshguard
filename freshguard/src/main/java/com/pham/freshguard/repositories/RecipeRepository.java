package com.pham.freshguard.repositories;

import com.pham.freshguard.domain.entities.RecipeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<RecipeEntity, Long> {
    @Query("SELECT DISTINCT r FROM RecipeEntity r JOIN r.ingredients i WHERE i.id IN :itemIds")
    List<RecipeEntity> findRecipesContainingIngredients(@Param("itemIds") List<Long> itemIds);
}
