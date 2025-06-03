package com.food.labeling.service;

import com.food.labeling.payload.FoodResponse;

import java.util.List;

public interface FoodService {

    FoodResponse searchFoodsByLabels(List<String> labels, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    void assignLabelToFood(Long foodId, Long labelId);

    void unassignLabelFromFood(Long foodId, Long labelId);
}
