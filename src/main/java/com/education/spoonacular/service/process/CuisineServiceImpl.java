package com.education.spoonacular.service.process;


import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.entity.Cuisine;
import com.education.spoonacular.repository.CuisineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CuisineServiceImpl implements CuisineService {
    private final CuisineRepository cuisineRepository;

    @Override
    public Set<String> filter(List<RecipeDto> recipeDtos) {
        Set<String> cuisineNames = recipeDtos.stream()
                .map(RecipeDto::getCuisines)
                .flatMap(Collection::stream).collect(Collectors.toSet());

        List<Cuisine> existingInDBCuisines = findExistingInDB(cuisineNames);
        if(existingInDBCuisines!=null){
            Set<String> existingCuisines = existingInDBCuisines.stream().map(Cuisine::getName).collect(Collectors.toSet());
            cuisineNames.removeAll(existingCuisines);
        }
        return cuisineNames;
    }
    ////TODO; the same for Cuisine, use distinct as now call DB as many times as duplicates I have (for example European 10 times 10 calls cuisineService.findByName)
    //// and move to CuisineService, rename process for saveAll()
    @Override
    public List<Cuisine> saveAll(Set<String> cuisines) {
        Set<Cuisine> cuisineList = cuisines.stream().map(s -> {
            Cuisine cuisine = new Cuisine();
            cuisine.setName(s);
            return cuisine;
        }).collect(Collectors.toSet());

        return cuisineRepository.saveAll(cuisineList);
    }

    @Override
    public Optional<Cuisine> findByName(String name) {
        return cuisineRepository.findByName(name);
    }

    @Override
    public List<Cuisine> findExistingInDB(Set<String> cuisineList) {
        return cuisineRepository.findExistingInDB(cuisineList);
    }
}
