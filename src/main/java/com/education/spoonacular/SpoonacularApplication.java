package com.education.spoonacular;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
//@EnableConfigurationProperties
public class SpoonacularApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpoonacularApplication.class, args);

    }

}
