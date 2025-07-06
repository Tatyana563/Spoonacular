package com.education.spoonacular.service.exception;

public class MissingCuisinesException extends RuntimeException {
    public MissingCuisinesException(String recipeName, String url) {
        super(String.format("Cuisines were not found for recipe with name '%s' and url: '%s'", recipeName, url));
    }
}
