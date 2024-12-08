package com.education.spoonacular.controller;

import com.education.spoonacular.dto.DishDto;
import com.education.spoonacular.dto.LunchRequestDto;
import com.education.spoonacular.service.process.api.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LunchSuggestionController {

    @Autowired
    private RecipeService recipeService;

    @PostMapping("/suggest-lunch")
    public ResponseEntity<List<DishDto>> suggestLunch(@RequestBody LunchRequestDto request) {
        List<DishDto> suggestedDishes = recipeService.getSuggestedDishes(request);
        return ResponseEntity.ok(suggestedDishes);
    }
}