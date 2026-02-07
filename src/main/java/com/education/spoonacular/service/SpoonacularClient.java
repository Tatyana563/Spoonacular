package com.education.spoonacular.service;

import com.education.spoonacular.dto.fetch.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "spoonacularClient", url = "${spoon.base-url}")
public interface SpoonacularClient {

    @GetMapping
    ResponseDto getDataByDishAndAmount(
            @RequestParam("query") String dish,
            @RequestParam("number") int amount,
            @RequestParam("addRecipeInformation") boolean addRecipeInformation,
            @RequestParam("addRecipeNutrition") boolean addRecipeNutrition,
            @RequestParam("apiKey") String apiKey
    );
}
