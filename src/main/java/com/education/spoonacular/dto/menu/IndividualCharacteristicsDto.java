package com.education.spoonacular.dto.menu;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndividualCharacteristicsDto {
    private Gender gender;
    private ActivityLevel activityLevel;
    private Goal goal;
    private Double height;
    private Double weight;
    private int age;
}
