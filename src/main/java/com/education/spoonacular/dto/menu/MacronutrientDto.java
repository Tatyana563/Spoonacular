package com.education.spoonacular.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MacronutrientDto {
    private double protein;
    private double fat;
    private double carbohydrates;
}
