package com.education.spoonacular.db_view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "recipe_nutrient_view")
public class RecipeNutrientView {

    @Id
    @Column(name = "recipe_id")
    private Long recipeId;

    @Column(name = "recipe_name")
    private String recipeName;

    @Column(name = "dish_type")
    private String dishType;

    @Column(name = "cuisine_name")
    private String cuisineName;

    @Column(name = "nutrient", columnDefinition = "jsonb")
    private String nutrient;

    @Column(name = "ingredient", columnDefinition = "jsonb")
    private String ingredient;
}

