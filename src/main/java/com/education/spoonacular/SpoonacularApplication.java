package com.education.spoonacular;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class SpoonacularApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpoonacularApplication.class, args);

    }

}
