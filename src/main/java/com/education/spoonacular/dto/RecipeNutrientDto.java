package com.education.spoonacular.dto;

import com.education.spoonacular.entity.Nutrient;
import com.education.spoonacular.entity.Recipe;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class RecipeNutrientDto {

    private Recipe recipe;
    private Nutrient nutrient;
    private Double amount;
    public RecipeNutrientDto(Nutrient nutrient, Double amount) {
        this.nutrient = nutrient;
        this.amount = amount;
    }
}

