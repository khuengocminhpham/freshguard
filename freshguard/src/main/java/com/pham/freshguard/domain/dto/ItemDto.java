package com.pham.freshguard.domain.dto;

import com.pham.freshguard.domain.entities.RecipeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {
    private Long id;

    private String name;
    private String category; // "Dairy", "Meat", "Vegetable", etc.
    private LocalDate expirationDate;
    private LocalDate purchaseDate;
    private Integer quantity;
    private String unit; // "pieces", "cups", "lbs"
    private String location; // "Fridge", "Pantry"
    private String notes;
    private Set<RecipeDto> recipes;
}
