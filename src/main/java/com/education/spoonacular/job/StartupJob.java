package com.education.spoonacular.job;

import com.education.spoonacular.config.JobConfig;
import com.education.spoonacular.config.JobProperties;
import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.dto.ResponseDto;
import com.education.spoonacular.service.process.api.MainService;
import com.education.spoonacular.service.search.SpoonSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupJob implements ApplicationRunner {

    private final SpoonSearchService spoonSearchService;
    private final MainService mainService;
    private final JobConfig jobConfig;
//TODO: create separate job for each category, add batch size

    private List<RecipeDto> fetchData() {
        log.info("Job running after application start...");
        List<RecipeDto> recipeDtos = new ArrayList<>();
        for (Map.Entry<String, JobProperties> entry : jobConfig.getJobs().entrySet()) {
            String dish = entry.getKey();
            JobProperties value = entry.getValue();
            ResponseDto dataByDishAndAmount = spoonSearchService.getDataByDishAndAmount(dish, value.getAmount());
            List<RecipeDto> recipeDto = dataByDishAndAmount.getResults();
            recipeDtos.addAll(recipeDto);
        }

        return recipeDtos;
    }

    private void processData(List<RecipeDto> recipeDtos) {
        //TODO: combine 2 filter methods in 1 and use Predicate as constants
        Stream<RecipeDto> filteredRecipes = filterIncompleteRecipes(recipeDtos);
        Stream<RecipeDto> filteredWithoutDuplicates = removeDuplicates(filteredRecipes);
        mainService.processResponse(filteredWithoutDuplicates.collect(Collectors.toList()));
    }
//TODO: filter recipes if Calories and so on in null;
    private Stream<RecipeDto> filterIncompleteRecipes(List<RecipeDto> recipeDtos) {
        return recipeDtos.stream()
                .filter(recipeDto -> !recipeDto.getUrl().isEmpty());

    }

    public <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet(); // Thread-safe Set for uniqueness
        return t -> seen.add(keyExtractor.apply(t));
    }

    public Stream<RecipeDto> removeDuplicates(Stream<RecipeDto> recipes) {
        return recipes
                .filter(distinctByKey(RecipeDto::getUrl));
    }

    @Override
    public void run(ApplicationArguments args) {
        List<RecipeDto> recipeDtoList = fetchData();
        processData(recipeDtoList);
    }

}






