package com.education.spoonacular.dto.menu;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MealType {
    BREAKFAST("breakfast"),
    LUNCH("lunch"),
    DINNER("dinner");

    String name;
}
