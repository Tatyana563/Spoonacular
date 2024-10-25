package com.education.spoonacular.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum Cuisine {
    MEDITERRANEAN("Mediterranean"),
    ITALIAN("Italian"),
    EUROPEAN("European"),
    GERMAN("German");

    private final String name;

    Cuisine(String name) {
        this.name = name;
    }

    @JsonCreator  // Converts JSON back to enum based on name
    public static Cuisine fromName(String name) {
        for (Cuisine cuisine : Cuisine.values()) {
            if (cuisine.name.equalsIgnoreCase(name)) {
                return cuisine;
            }
        }
        throw new IllegalArgumentException("Unknown cuisine name: " + name);
    }
}
