package com.food.labeling.service;

import com.food.labeling.payload.FoodDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FoodService {
    Page<FoodDTO> getAllFoods(Pageable pageable);

    Page<FoodDTO> getFoodsWithAllLabels(List<String> labels, Pageable pageable);
}
