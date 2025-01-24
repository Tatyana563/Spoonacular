package com.education.spoonacular.dto.fetch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishDto {
    private String name;
    private String description;
    private List<String> cuisines;
    private List<String> ingredients;
    private NutritionalInfoDto nutrition;
}
