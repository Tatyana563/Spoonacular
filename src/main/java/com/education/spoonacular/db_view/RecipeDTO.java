package com.education.spoonacular.db_view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class RecipeDTO {
    private Integer recipeId;
    private String recipeName;
    private String dishType;
    private Set<Integer> cuisineName;
    private List<ViewNutrient> nutrient;
    private List<ViewIngredient> ingredient;

}

