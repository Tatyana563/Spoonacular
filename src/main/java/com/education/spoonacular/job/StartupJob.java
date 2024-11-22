package com.education.spoonacular.job;

import com.education.spoonacular.config.JobConfig;
import com.education.spoonacular.config.JobProperties;
import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.dto.ResponseDto;
import com.education.spoonacular.service.process.RecipeServiceImpl;
import com.education.spoonacular.service.search.SpoonSearchServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupJob {
    private final SpoonSearchServiceImpl spoonSearchService;
    private final RecipeServiceImpl recipeService;
    private final JobConfig jobConfig;

    @PostConstruct
    public void runAtStartup() {
        log.info("Job running after application start...");

        for (Map.Entry<String, JobProperties> entry : jobConfig.getJobs().entrySet()) {
            String dish = entry.getKey();
            JobProperties value = entry.getValue();
            ResponseDto dataByDishAndAmount = spoonSearchService.getDataByDishAndAmount(dish, value.getAmount());
            List<RecipeDto> recipeDtos = dataByDishAndAmount.getResults();

            recipeService.processResponse(recipeDtos);
            log.info("Job completed for {}", dish);

        }

    }

}






