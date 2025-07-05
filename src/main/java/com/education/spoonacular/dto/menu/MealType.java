package com.education.spoonacular.dto.menu;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum MealType {
    BREAKFAST("breakfast"),
    LUNCH("lunch"),
    DINNER("dinner");

    String name;
    public static MealType fromString(String value) {
        return Arrays.stream(MealType.values())
                .filter(m -> m.name.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid MealType: " + value));
    }
}
