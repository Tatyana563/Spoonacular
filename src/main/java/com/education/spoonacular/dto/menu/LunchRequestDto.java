package com.education.spoonacular.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LunchRequestDto {
    private String gender; // "man" or "woman"
    private String activityLevel; // "active" or "not_active"
    private Set<String> cuisinePreferences;
    private Set<Integer> ingredientsExclusions;
}
