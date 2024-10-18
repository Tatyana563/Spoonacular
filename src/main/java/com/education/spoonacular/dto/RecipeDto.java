package com.education.spoonacular.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeDto {
    @JsonProperty("title")
    private String name;
    private String summary;
    private Set<String> cuisines;
    private boolean vegetarian;
    @JsonProperty("readyInMinutes")
    private String preparationTime;
}
