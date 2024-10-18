package com.education.spoonacular.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NutrientDto {
    private String name;
    private String unit;
    private Double amount;
}

