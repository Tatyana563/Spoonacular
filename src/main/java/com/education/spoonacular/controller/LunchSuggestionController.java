package com.education.spoonacular.controller;

import com.education.spoonacular.dto.DishDto;
import com.education.spoonacular.dto.LunchRequestDto;
import com.education.spoonacular.service.process.api.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LunchSuggestionController {

    private final MenuService menuService;

    @PostMapping("/suggest-lunch")
    public List<DishDto> suggestLunch(@RequestBody LunchRequestDto request) {
      return menuService.getSuggestedDishes(request);

    }
}