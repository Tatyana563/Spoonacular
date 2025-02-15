package com.education.spoonacular.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LunchRequestDto {
private IndividualCharacteristicsDto characteristicsDto;
    private List<Integer> cuisinePreferences;
    private Set<String> ingredientsExclusions;
}
