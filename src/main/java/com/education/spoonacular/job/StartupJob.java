package com.education.spoonacular.job;

import com.education.spoonacular.config.JobConfig;
import com.education.spoonacular.config.JobProperties;
import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.dto.ResponseDto;
import com.education.spoonacular.service.process.api.MainService;
import com.education.spoonacular.service.search.SpoonSearchServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupJob implements ApplicationRunner {
    //TODO: add interface
    private final SpoonSearchServiceImpl spoonSearchService;
    private final MainService mainService;
    private final JobConfig jobConfig;

    //TODO: use application runner instead of  @PostConstruct
    private List<RecipeDto> fetchData() {
        log.info("Job running after application start...");
        List<RecipeDto> recipeDtos = new ArrayList<>();
        for (Map.Entry<String, JobProperties> entry : jobConfig.getJobs().entrySet()) {
            String dish = entry.getKey();
            JobProperties value = entry.getValue();
            ResponseDto dataByDishAndAmount = spoonSearchService.getDataByDishAndAmount(dish, value.getAmount());
            List<RecipeDto> recipeDto = dataByDishAndAmount.getResults();
            //TODO in case of duplicates, create Set for keys
            recipeDtos.addAll(recipeDto);
//TODO: filter incomplete recipes
        }
        return recipeDtos;
    }

    private void processData(List<RecipeDto> recipeDtos) {
        mainService.processResponse(recipeDtos);
    }


    @Override
    public void run(ApplicationArguments args) {
        List<RecipeDto> recipeDtoList = fetchData();
        processData(recipeDtoList);
    }
}






