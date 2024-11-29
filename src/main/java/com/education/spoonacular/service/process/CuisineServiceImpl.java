package com.education.spoonacular.service.process;


import com.education.spoonacular.dto.RecipeDto;
import com.education.spoonacular.entity.Cuisine;
import com.education.spoonacular.repository.CuisineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CuisineServiceImpl implements CuisineService {
    private final CuisineRepository cuisineRepository;

    @Override
    public void collectAndSaveNewEntities(List<RecipeDto> recipeDtos) {
        List<String> cuisineNames = recipeDtos.stream()
                .map(RecipeDto::getCuisines)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        List<String> existingCuisineNamesInDB = findExistingCuisineNamesInDB(cuisineNames);
        if(existingCuisineNamesInDB!=null){
            cuisineNames.removeAll(existingCuisineNamesInDB);
        }
        List<Cuisine> cuisines = cuisineNames.stream().map(Cuisine::new).collect(Collectors.toList());
        cuisineRepository.saveAll(cuisines);
    }


    @Override
    public List<Cuisine> saveAll(List<String> cuisines) {
        List<Cuisine> cuisineList = cuisines.stream().map(s -> {
            Cuisine cuisine = new Cuisine();
            cuisine.setName(s);
            return cuisine;
        }).collect(Collectors.toList());

        return cuisineRepository.saveAll(cuisineList);
    }

    @Override
    public List<Cuisine> findByNames(List<String> names) {
        return cuisineRepository.findByNames(names);
    }

    @Override
    public List<String> findExistingCuisineNamesInDB(List<String> cuisineList) {
        return cuisineRepository.findExistingCuisineNamesInDB(cuisineList);
    }

}
