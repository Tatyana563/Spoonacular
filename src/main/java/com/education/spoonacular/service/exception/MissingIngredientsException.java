package com.education.spoonacular.service.exception;

public class MissingIngredientsException extends RuntimeException {
    public MissingIngredientsException(String recipeName, String url) {
        super(String.format("Ingredients amount from DB is not equivalent to the required for recipe with name '%s' and url: '%s'", recipeName, url));
    }
}