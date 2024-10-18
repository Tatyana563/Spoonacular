package com.education.spoonacular.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Validated
@ConfigurationProperties(prefix = "spoon")
public class SpoonProperties {
    private String baseUrl;
    private String apiKey;
}
