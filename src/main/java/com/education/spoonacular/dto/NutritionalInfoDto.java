package com.education.spoonacular.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NutritionalInfoDto {
    private int calories;
    private int protein;
    private int carbs;
    private int fat;
}