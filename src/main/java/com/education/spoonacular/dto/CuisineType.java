package com.education.spoonacular.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum CuisineType {
    @JsonProperty("Mediterranean")
    MEDITERRANEAN,
    @JsonProperty("Italian")
    ITALIAN,
    @JsonProperty("European")
    EUROPEAN,
    @JsonProperty("German")
    GERMAN;
}
