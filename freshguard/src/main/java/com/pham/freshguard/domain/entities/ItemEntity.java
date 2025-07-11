package com.pham.freshguard.domain.entities;

import jakarta.persistence.*;
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
@Entity
@Table(name = "items")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_id_seq")
    private Long id;

    private String name;
    private String category; // "Dairy", "Meat", "Vegetable", etc.
    private LocalDate expirationDate;
    private LocalDate purchaseDate;
    private Integer quantity;
    private String unit; // "pieces", "cups", "lbs"
    private String location; // "Fridge", "Pantry"
    private String notes;

    @ManyToMany(mappedBy = "ingredients")
    private Set<RecipeEntity> recipes;

}
