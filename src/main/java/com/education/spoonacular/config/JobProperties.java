package com.education.spoonacular.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobProperties {
    private int amount;
    //TODO: not an optional property, filter all recipes
    private boolean filterNotFullRecipes = false;

}
