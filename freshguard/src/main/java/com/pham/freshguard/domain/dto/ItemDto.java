package com.pham.freshguard.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;
    private Integer quantity;
    private String location; // "Fridge", "Pantry"
}
