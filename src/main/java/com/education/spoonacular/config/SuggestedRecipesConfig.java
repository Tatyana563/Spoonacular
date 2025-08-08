package com.education.spoonacular.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "suggested.recipes")
public class SuggestedRecipesConfig {
    private int amount;
}
