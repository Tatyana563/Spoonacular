package com.education.spoonacular.dto.menu;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LunchRequestDto {
    @Valid
    @NotNull(message = "characteristicsDto which includes gender, activityLevel, goal, height, weight is required")
    private IndividualCharacteristicsDto characteristicsDto;
    private List<Integer> cuisinePreferences;
    private Set<String> ingredientsExclusions;
}
