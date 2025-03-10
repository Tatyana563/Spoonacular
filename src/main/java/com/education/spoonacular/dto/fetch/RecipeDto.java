package com.education.spoonacular.dto.fetch;

import com.education.spoonacular.dto.menu.MealType;
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
public class RecipeDto implements Serializable {
    @JsonProperty("title")
    private String name;
    @JsonProperty("sourceUrl")
    private String url;
    private String summary;
    private boolean vegetarian;
    @JsonProperty("readyInMinutes")
    private int preparationTime;
    @JsonProperty("nutrition")
    private NutritionDto nutritionDto;
    private List<String> cuisines;
    private MealType mealType;
}
