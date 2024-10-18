package com.education.spoonacular.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "recipe_ingredient")
@NoArgsConstructor
public class RecipeIngredient  {

    @EmbeddedId
    private RecipeIngredientId id;

    @ManyToOne
    @MapsId("recipeid")
    @JoinColumn(name = "recipeid")
    private Recipe recipe;

    @ManyToOne
    @MapsId("ingredientid")
    @JoinColumn(name = "ingredientid")
    private Ingredient ingredient;

    @Column(name = "amount", nullable = false)
    private Double amount;

    public RecipeIngredient(Recipe recipe, Ingredient ingredient, Double amount) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.amount = amount;
        this.id = new RecipeIngredientId(recipe.getId(), ingredient.getId());
    }
}
