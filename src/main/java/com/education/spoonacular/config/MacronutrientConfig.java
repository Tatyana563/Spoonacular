package com.education.spoonacular.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "macronutrient")
public class MacronutrientConfig {
    private double proteinLowActivity;
    private double proteinHighActivity;
    private double fat;
    private double carbohydrates;
    private double carbohydratesMultiplier;
}
