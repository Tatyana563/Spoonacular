package com.education.spoonacular.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "recipe_nutrient")
@NoArgsConstructor
public class RecipeNutrient  {

    @EmbeddedId
    private RecipeNutrientId id;

    @ManyToOne
    @MapsId("recipeId")
    @JoinColumn(name = "recipeid")
    private Recipe recipe;
    @ManyToOne
    @MapsId("nutrientId")
    @JoinColumn(name = "nutrientid")
    private Nutrient nutrient;

    @Column(name = "amount", nullable = false)
    private Double amount;

    public RecipeNutrient(Recipe recipe, Nutrient nutrient, Double amount) {
        this.recipe=recipe;
        this.nutrient = nutrient;
        this.amount = amount;
        this.id = new RecipeNutrientId(recipe.getId(), nutrient.getId());
    }
}
