package com.education.spoonacular.dto.fetch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NutritionalInfoDto {
    private double calories;
    private double protein;
    private double carbs;
    private double fat;
}