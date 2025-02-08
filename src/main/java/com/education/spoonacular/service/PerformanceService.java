package com.education.spoonacular.service;

import com.education.spoonacular.dto.menu.LunchRequestDto;
import com.education.spoonacular.job.Timed;
import com.education.spoonacular.service.process.api.MenuService;
import org.springframework.stereotype.Service;

@Service
public class PerformanceService {
    @Timed
    public void testPerformance(LunchRequestDto request, MenuService menuService) {
        for (int i = 0; i < 10_000; i++) {
            menuService.getSuggestedDishes(request);
        }
    }
}

