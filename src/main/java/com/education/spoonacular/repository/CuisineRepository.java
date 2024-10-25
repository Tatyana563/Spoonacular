package com.education.spoonacular.repository;

import com.education.spoonacular.entity.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuisineRepository extends JpaRepository<Cuisine, Integer> {
    Optional<Cuisine> findByName(String name);

}
