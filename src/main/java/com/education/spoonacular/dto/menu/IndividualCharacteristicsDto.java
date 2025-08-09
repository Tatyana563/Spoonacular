package com.education.spoonacular.dto.menu;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndividualCharacteristicsDto {
    @NotNull(message = "Gender is required")
    private Gender gender;
    @NotNull(message = "Activity level is required")
    private ActivityLevel activityLevel;
    @NotNull(message = "Goal is required")
    private Goal goal;
    @Min(value = 100, message = "Height must be greater than 100")
    @NotNull(message = "Height is required")
    private Double height;
    @Min(value = 10, message = "Weight must be greater than 10")
    @NotNull(message = "Weight is required")
    private Double weight;
    @Min(value = 18, message = "Age must be greater than 18")
    private int age;
}
