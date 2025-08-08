package com.education.spoonacular.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "activity.level")
public class ActivityLevelConfig {
    private double sedentary;
    private double lightlyActive;
    private double moderatelyActive;
    private double veryActive;
    private double superActive;
}
