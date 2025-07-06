package com.education.spoonacular.service.exception;

public class InvalidMealTypeException extends RuntimeException {
    public InvalidMealTypeException(String dish) {
        super("MealType can't be determined for dish: " + dish +
                " as dish is not in the allowed list");
    }
}
