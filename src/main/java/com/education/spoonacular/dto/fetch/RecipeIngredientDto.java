package com.education.spoonacular.dto.fetch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeIngredientDto implements Serializable {
    private String name;
    private String unit;
    private Double amount;

}

