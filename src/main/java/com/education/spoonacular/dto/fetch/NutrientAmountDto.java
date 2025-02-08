package com.education.spoonacular.dto.fetch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class NutrientAmountDto {
  private   String name;
  private   Double amount;
  private   String unit;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NutrientAmountDto that = (NutrientAmountDto) o;

    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    return amount != null ? amount.equals(that.amount) : that.amount == null;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (amount != null ? amount.hashCode() : 0);
    return result;
  }
}
