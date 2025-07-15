package com.pham.freshguard.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "items")
@EqualsAndHashCode(exclude = "recipes")
@ToString(exclude = "recipes")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_id_seq")
    private Long id;

    private String name;
    private String category; // "Dairy", "Meat", "Vegetable", etc.
    private LocalDate expirationDate;
    private LocalDate purchaseDate;
    private Integer quantity;
    private String location; // "Fridge", "Pantry"

    @ManyToMany(mappedBy = "ingredients")
    private Set<RecipeEntity> recipes;
}
