package com.education.spoonacular.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RecipeDTOView {
    private Integer recipeId;
    private String recipeName;
    private String dishType;
    private String cuisinesJson;   // JSON string of cuisines
    private String nutrientJson;   // JSON string of nutrients
    private String ingredientJson; // JSON string of ingredients
}

