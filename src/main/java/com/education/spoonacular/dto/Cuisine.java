package com.education.spoonacular.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum Cuisine {
    MEDITERRANEAN("Mediterranean"),
    ITALIAN("Italian"),
    EUROPEAN("European");

    private final String name;

    Cuisine(String name) {
        this.name = name;
    }
}
