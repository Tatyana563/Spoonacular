package com.education.spoonacular.service.search;

import com.education.spoonacular.dto.ResponseDto;

public interface SpoonSearchProcess {
    ResponseDto getDataByDishAndAmount(String dish, int amount);
}
