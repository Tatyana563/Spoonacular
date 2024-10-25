package com.education.spoonacular.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobProperties {
    private String name = "";
    private int amount;
    private String cron = "-";
    private boolean filterNotFullRecipes = false;

}
