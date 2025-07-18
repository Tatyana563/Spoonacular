package com.education.spoonacular.job;

import com.education.spoonacular.config.JobConfig;
import com.education.spoonacular.config.JobProperties;
import com.education.spoonacular.dto.fetch.NutritionDto;
import com.education.spoonacular.dto.fetch.RecipeDto;
import com.education.spoonacular.dto.fetch.ResponseDto;
import com.education.spoonacular.dto.menu.MealType;
import com.education.spoonacular.service.exception.InvalidMealTypeException;
import com.education.spoonacular.service.process.api.MainService;
import com.education.spoonacular.service.search.SpoonSearchService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupJob implements ApplicationRunner {

    @Value("${recipe.job.source}")
    private String recipeSource;
    private static final List<String> BREAKFAST_DISHES = Arrays.asList("pancakes");
    private static final List<String> LUNCH_DISHES = Arrays.asList("soup", "pasta");
    private static final List<String> DINNER_DISHES = Arrays.asList("salad");

    public static final Map<String, List<String>> MEAL_DISH;

    static {
        MEAL_DISH = Map.of("Breakfast", BREAKFAST_DISHES, "Lunch", LUNCH_DISHES, "Dinner", DINNER_DISHES);
    }

    private final SpoonSearchService spoonSearchService;
    private final MainService mainService;
    private final JobConfig jobConfig;
    private static final List<String> REQUIRED_NUTRIENTS = List.of("Calories", "Carbohydrates", "Protein", "Fat");

    private List<RecipeDto> fetchData() {
        log.info("Job running after application start...");
        List<RecipeDto> recipeDtos = new ArrayList<>();
        for (Map.Entry<String, JobProperties> entry : jobConfig.getJobs().entrySet()) {
            String dish = entry.getKey();
            JobProperties value = entry.getValue();
            ResponseDto dataByDishAndAmount = spoonSearchService.getDataByDishAndAmount(dish, value.getAmount());
            List<RecipeDto> recipeDto = dataByDishAndAmount.getResults();
            MealType mealType = determineMealType(dish);
            recipeDto.stream().forEach(recipeDto1 -> recipeDto1.setMealType(mealType));
            recipeDtos.addAll(recipeDto);
        }
        return recipeDtos;
    }

    private MealType determineMealType(String dish) {
        return MEAL_DISH.entrySet().stream()
                .filter(entry -> entry.getValue().contains(dish))
                .map(entry -> MealType.fromString(entry.getKey()))
                .findFirst()
                .orElseThrow(() -> new InvalidMealTypeException(dish));
    }

    private void processData(List<RecipeDto> recipeDtos) {
        Stream<RecipeDto> filteredRecipes = filterRecipes(recipeDtos);
        mainService.processResponse(filteredRecipes.collect(Collectors.toList()));
    }

    private Stream<RecipeDto> filterRecipes(List<RecipeDto> recipeDtos) {
        return recipeDtos.stream()
                .filter(filterRecipesWithEmptyUrls())
                .filter(filterDuplicates(RecipeDto::getUrl))
                .filter(this::hasCompleteNutrients);
    }

    private Predicate<RecipeDto> filterRecipesWithEmptyUrls() {
        return recipeDto -> !recipeDto.getUrl().isEmpty();
    }

    private <T> Predicate<T> filterDuplicates(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = new HashSet<>();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private boolean hasCompleteNutrients(RecipeDto recipeDto) {

        return Stream.of(recipeDto)
                .filter(Objects::nonNull)
                .map(RecipeDto::getNutritionDto)
                .filter(Objects::nonNull)
                .map(NutritionDto::getRecipeNutrientDtoList)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(recipeNutrientDto -> REQUIRED_NUTRIENTS.contains(recipeNutrientDto.getName()))
                .filter(recipeNutrientDto -> recipeNutrientDto.getAmount() != null && recipeNutrientDto.getUnit() != null)
                .count() == REQUIRED_NUTRIENTS.size();

    }

    @Override
    public void run(ApplicationArguments args) throws IOException {
        log.info("Running job on application startup...");
        executeJob();
    }

    // Run every Monday at 1:00 AM
    @Scheduled(cron = "0 0 1 ? * MON")
    public void scheduledRun() {
        log.info("Running weekly scheduled job...");
        executeJob();
    }

    private void executeJob() {
        try {
            List<RecipeDto> recipes;
            if ("remote".equalsIgnoreCase(recipeSource)) {
                recipes = fetchData();
                log.info("Fetched {} recipes remotely.", recipes.size());
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                recipes = objectMapper.readValue(new File("recipes.json"),
                        new TypeReference<List<RecipeDto>>() {
                        });
                log.info("Loaded {} recipes from file.", recipes.size());
            }

            processData(recipes);

        } catch (IOException e) {
            log.error("Error executing job", e);
        }
    }
}






