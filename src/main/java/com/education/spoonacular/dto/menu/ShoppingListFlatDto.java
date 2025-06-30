package com.education.spoonacular.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShoppingListFlatDto {
    private Integer recipeId;
    private String dish;
    private String ingredientName;
    private String ingredientUnit;
    private Double ingredientAmount;

}
