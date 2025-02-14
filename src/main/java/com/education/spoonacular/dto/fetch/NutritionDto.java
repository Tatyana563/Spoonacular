package com.education.spoonacular.dto.fetch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NutritionDto implements Serializable {
    @JsonProperty("nutrients")
    List<RecipeNutrientDto> recipeNutrientDtoList;
    @JsonProperty("ingredients")
    private List<RecipeIngredientDto> recipeIngredientDto;
}
