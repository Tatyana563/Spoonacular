package com.education.spoonacular.dto.menu;

import com.education.spoonacular.dto.RecipeNutrientDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ShoppingListDto {
    private int id;
    private String dish;
    private List<RecipeNutrientDto> ingredients;

}
