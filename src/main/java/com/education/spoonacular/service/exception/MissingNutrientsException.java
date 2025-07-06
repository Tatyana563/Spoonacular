package com.education.spoonacular.service.exception;

public class MissingNutrientsException extends RuntimeException {
    public MissingNutrientsException(String recipeName, String url) {
        super(String.format("Nutrients amount from DB is not equivalent to the required for recipe with name '%s' and url: '%s'", recipeName, url));
    }
}
