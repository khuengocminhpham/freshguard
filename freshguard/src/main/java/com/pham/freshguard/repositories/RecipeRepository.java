package com.pham.freshguard.repositories;

import com.pham.freshguard.domain.entities.RecipeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends CrudRepository<RecipeEntity, Long> {
}
