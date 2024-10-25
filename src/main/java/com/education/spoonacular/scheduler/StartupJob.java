package com.education.spoonacular.scheduler;

import com.education.spoonacular.config.JobConfig;
import com.education.spoonacular.config.JobProperties;
import com.education.spoonacular.dto.Cuisine;
import com.education.spoonacular.dto.NutrientDto;
import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.dto.RecipeNutrientDto;
import com.education.spoonacular.entity.Nutrient;
import com.education.spoonacular.entity.Recipe;
import com.education.spoonacular.service.process.NutrientServiceImpl;
import com.education.spoonacular.service.process.RecipeServiceImpl;
import com.education.spoonacular.service.search.SpoonSearchServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupJob {
    private final SpoonSearchServiceImpl spoonSearchService;
    private final RecipeServiceImpl recipeService;
    private final NutrientServiceImpl nutrientService;
    private final JobConfig jobConfig;

    @PostConstruct
    public void runAtStartup() throws JsonProcessingException {
        log.info("Job running after application start...");
        ObjectMapper objectMapper = new ObjectMapper();
        for (Map.Entry<String, JobProperties> entry : jobConfig.getJobs().entrySet()) {
            String key = entry.getKey();
            JobProperties value = entry.getValue();
            String jsonData = spoonSearchService.getDataByDishAndAmount(key, value.getAmount());

            JsonNode results = objectMapper.readTree(jsonData).get("results");

            for (JsonNode result : results) {
                List<NutrientDto> nutrients = objectMapper.readValue(
                        result.get("nutrition").get("nutrients").toString(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, NutrientDto.class)
                );
                List<RecipeNutrientDto> recipeNutrients = new ArrayList<>();
                for (NutrientDto nutrientDto : nutrients) {
                    Nutrient existingNutrient = nutrientService.findByName(nutrientDto.getName());
                    if (existingNutrient == null) {
                        nutrientService.save(nutrientDto);
                    }

                    RecipeNutrientDto recipeNutrient = new RecipeNutrientDto(nutrientService.findByName(nutrientDto.getName()), nutrientDto.getAmount());
                    recipeNutrients.add(recipeNutrient);
                }

                List<Cuisine> cuisines = objectMapper.readValue(
                        result.get("cuisines").toString(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Cuisine.class)
                );

                RecipeDto recipeDto = objectMapper.readValue(result.toString(), RecipeDto.class);

                Recipe existingRecipe = recipeService.findByName(recipeDto.getName());
                if (existingRecipe == null) {

                    recipeDto.setRecipeNutrientDtos(recipeNutrients);
                    recipeDto.setCuisines(cuisines);
                    recipeService.save(recipeDto);
                }

            }
        }

    }
}





