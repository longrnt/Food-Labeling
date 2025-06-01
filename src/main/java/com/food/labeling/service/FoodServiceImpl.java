package com.food.labeling.service;

import com.food.labeling.model.Food;
import com.food.labeling.model.Label;
import com.food.labeling.payload.FoodDTO;
import com.food.labeling.repository.FoodRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;

    public FoodServiceImpl(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Override
    public Page<FoodDTO> getFoodsWithAllLabels(List<String> labels, Pageable pageable) {
        Page<Long> foodIds = foodRepository.findFoodIdsMatchingAllLabels(labels, labels.size(), pageable);
        List<Food> foods = foodRepository.findAllById(foodIds.getContent());

        List<FoodDTO> dtos = foods.stream()
                .map(f -> new FoodDTO(
                        f.getFoodId(),
                        f.getFoodName(),
                        f.getLabels().stream().map(Label::getLabelName).toList()
                ))
                .toList();

        return new PageImpl<>(dtos, pageable, foodIds.getTotalElements());
    }

    @Override
    public Page<FoodDTO> getAllFoods(Pageable pageable) {
        Page<Food> foodPage = foodRepository.findAll(pageable);

        List<FoodDTO> dtos = foodPage.stream()
                .map(f -> new FoodDTO(
                        f.getFoodId(),
                        f.getFoodName(),
                        f.getLabels().stream().map(Label::getLabelName).toList()
                ))
                .toList();

        return new PageImpl<>(dtos, pageable, foodPage.getTotalElements());
    }
}
