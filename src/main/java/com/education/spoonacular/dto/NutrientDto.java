package com.education.spoonacular.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class NutrientDto {
    private String name;
    private String unit;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NutrientDto that = (NutrientDto) o;

        if (!name.equals(that.name)) return false;
        return unit.equals(that.unit);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + unit.hashCode();
        return result;
    }
}

