package com.education.spoonacular.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "metabolic-rate")
public class MetabolicRateConfig {
    private int maleConstant;
    private int femaleConstant;
    private double weightMultiplier;
    private double heightMultiplier;
    private double ageMultiplier;
}