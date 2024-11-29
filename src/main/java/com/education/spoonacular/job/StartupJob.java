package com.education.spoonacular.job;

import com.education.spoonacular.config.JobConfig;
import com.education.spoonacular.config.JobProperties;
import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.dto.ResponseDto;
import com.education.spoonacular.service.process.MainServiceImpl;
import com.education.spoonacular.service.search.SpoonSearchServiceImpl;
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
    private final MainServiceImpl mainService;
    private final JobConfig jobConfig;

    @PostConstruct
    public void process() {
        List<RecipeDto> recipeDtoList = fetchData();
        processData(recipeDtoList);
    }

    //TODO: use application runner instead of  @PostConstruct
    private List<RecipeDto> fetchData() {
        log.info("Job running after application start...");
        List<RecipeDto> recipeDtos = new ArrayList<>();
        for (Map.Entry<String, JobProperties> entry : jobConfig.getJobs().entrySet()) {
            List<RecipeDto> recipeDto= new ArrayList<>();
            String dish = entry.getKey();
            JobProperties value = entry.getValue();
            ResponseDto dataByDishAndAmount = spoonSearchService.getDataByDishAndAmount(dish, value.getAmount());
            recipeDto = dataByDishAndAmount.getResults();
            //TODO in case of duplicates
            recipeDtos.addAll(recipeDto);
//TODO: filter incomplete recipes
        }
        return recipeDtos;
    }

    private void processData(List<RecipeDto> recipeDtos) {
        mainService.processResponse(recipeDtos);
    }


//TODO: check if index is required for cuisine if it has unique
}






