package com.education.spoonacular.service.search;

import com.education.spoonacular.dto.fetch.ResponseDto;

public interface SpoonSearchService {
    ResponseDto getDataByDishAndAmount(String dish, int amount);
}
