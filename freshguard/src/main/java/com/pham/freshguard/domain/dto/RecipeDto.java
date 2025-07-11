package com.pham.freshguard.domain.dto;

import com.pham.freshguard.domain.entities.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeDto {
    private Long id;

    private String name;
    private String description;
    private String instructions;
    private Integer servings;
    private Integer prepTimeMinutes;
    private LocalDateTime createdAt;

    private Set<ItemDto> ingredients;
}
