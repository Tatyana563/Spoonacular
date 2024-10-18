package com.education.spoonacular.repository;

import com.education.spoonacular.entity.Cuisine;
import com.education.spoonacular.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuisineRepository extends JpaRepository<Cuisine, Integer>  {
    Cuisine findByName(String name);
}
