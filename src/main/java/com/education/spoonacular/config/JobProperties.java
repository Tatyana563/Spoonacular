package com.education.spoonacular.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobProperties {
    private String name = "";
    private int amount;
    private boolean filterNotFullRecipes = false;

}
