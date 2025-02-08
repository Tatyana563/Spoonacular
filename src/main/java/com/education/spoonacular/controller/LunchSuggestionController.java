package com.education.spoonacular.controller;

import com.education.spoonacular.db_view.RecipeDTO;
import com.education.spoonacular.dto.menu.LunchRequestDto;
import com.education.spoonacular.dto.menu.ShoppingListDto;
import com.education.spoonacular.job.Timed;
import com.education.spoonacular.service.PerformanceService;
import com.education.spoonacular.service.process.api.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LunchSuggestionController {
    private final PerformanceService performanceService;
    private final MenuService menuService;

    @PostMapping("/suggest-lunch")
    public List<RecipeDTO> suggestLunch(@RequestBody LunchRequestDto request) {
        performanceService.testPerformance(request, menuService);

        return menuService.getSuggestedDishes(request);

    }

    @PostMapping("/shopping-list")
    public List<ShoppingListDto> getShoppingListForSuggestedLunch(@RequestBody Set<Integer> dishIds) {
        return menuService.getShoppingList(dishIds);

    }

}