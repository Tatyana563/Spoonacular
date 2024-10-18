package com.education.spoonacular.scheduler;

import com.education.spoonacular.dto.CuisineDto;
import com.education.spoonacular.dto.NutrientDto;
import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.entity.Cuisine;
import com.education.spoonacular.entity.Nutrient;
import com.education.spoonacular.entity.Recipe;
import com.education.spoonacular.entity.RecipeNutrient;
import com.education.spoonacular.service.process.CuisineService;
import com.education.spoonacular.service.process.NutrientServiceImpl;
import com.education.spoonacular.service.process.RecipeNutrientService;
import com.education.spoonacular.service.process.RecipeServiceImpl;
import com.education.spoonacular.service.search.SpoonSearchServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupJob {
    private final SpoonSearchServiceImpl spoonSearchService;
    private final RecipeServiceImpl recipeService;
    private final NutrientServiceImpl nutrientService;
    private final RecipeNutrientService recipeNutrientService;
    private final CuisineService cuisineService;

    @PostConstruct
    public void runAtStartup() throws JsonProcessingException {
        log.info("Job running after application start...");
        String data = spoonSearchService.getDataByDishAndAmount("pizza", 1);
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode results = objectMapper.readTree(data).get("results");

        for (JsonNode result : results) {
            List<NutrientDto> nutrients = objectMapper.readValue(
                    result.get("nutrition").get("nutrients").toString(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, NutrientDto.class)
            );
            JsonNode cuisines = result.get("cuisines");
            for (JsonNode cuisine : cuisines) {
                CuisineDto cuisineDto = objectMapper.readValue(cuisine.toString(), CuisineDto.class);
                Cuisine existingCuisine = cuisineService.findByName(cuisineDto.getName());

                if (existingCuisine == null) {
                    cuisineService.save(cuisineDto);
                }
            }

            RecipeDto recipeDto = objectMapper.readValue(result.toString(), RecipeDto.class);

            Recipe existingRecipe = recipeService.findByName(recipeDto.getName());
            if (existingRecipe == null) {
                recipeService.save(recipeDto);
            }

            for (NutrientDto nutrientDto : nutrients) {
                Nutrient existingNutrient = nutrientService.findByName(nutrientDto.getName());
                if (existingNutrient == null) {
                    nutrientService.save(nutrientDto);
                }

                RecipeNutrient recipeNutrient = new RecipeNutrient(recipeService.findByName(recipeDto.getName()), nutrientService.findByName(nutrientDto.getName()), nutrientDto.getAmount());

                recipeNutrientService.save(recipeNutrient);
            }

        }

    }
}
