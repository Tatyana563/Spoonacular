package com.education.spoonacular;

import org.flywaydb.core.internal.database.base.Connection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
@EnableConfigurationProperties
public class SpoonacularApplication {
    private static final String API_KEY = "1edca81c08ff414ebee59c30eb166696";
   // private static final String API_URL = "https://api.spoonacular.com/recipes/complexSearch";

    public static void main(String[] args) {
        SpringApplication.run(SpoonacularApplication.class, args);

//        RestTemplate restTemplate = new RestTemplate();
//
//        //   String url = "https://api.spoonacular.com/recipes/complexSearch?query=pizza&number=10&addRecipeInformation=true&apiKey=1edca81c08ff414ebee59c30eb166696";
//        String url = "https://api.spoonacular.com/recipes/658615/information?includeNutrition=false&apiKey=1edca81c08ff414ebee59c30eb166696";
//
//        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//        if (response.getStatusCodeValue() == 200) {
//            System.out.println("Response: " + response.getBody());
//        } else {
//            System.out.println("Failed to fetch data. HTTP error code: " + response.getStatusCodeValue());
//        }

    }

}
