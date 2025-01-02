package com.education.spoonacular.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NutritionDto {
    @JsonProperty("nutrients")
    List<RecipeNutrientDto> recipeNutrientDtoList;
    @JsonProperty("ingredients")
    private List<RecipeIngredientDto> recipeIngredientDto;
}
//TODO: divide into packages inner and outer dtos