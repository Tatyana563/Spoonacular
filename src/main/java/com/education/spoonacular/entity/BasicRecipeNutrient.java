package com.education.spoonacular.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class BasicRecipeNutrient {
    @EmbeddedId
    private RecipeNutrientId id;

    @ManyToOne
    @MapsId("recipeIdCompound")
    @JoinColumn(name = "recipeid", insertable = false, updatable = false)
    private Recipe recipe;

    @ManyToOne
    @MapsId("nutrientIdCompound")
    @JoinColumn(name = "nutrientid", insertable = false, updatable = false)
    private Nutrient nutrient;

    @Column(name = "amount", nullable = false)
    private Double amount;

}
