package com.education.spoonacular.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@Table(name = "recipe_ingredient")
public class RecipeIngredient {
    @ManyToOne
    @JoinColumn(name = "ingredientid")
    private Ingredient ingredient;
    @Column(name = "amount", nullable = false)
    private Double amount;
}
