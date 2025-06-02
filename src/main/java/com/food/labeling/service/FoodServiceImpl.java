package com.food.labeling.service;

import com.food.labeling.config.AppConstants;
import com.food.labeling.model.Food;
import com.food.labeling.model.Label;
import com.food.labeling.payload.FoodDTO;
import com.food.labeling.payload.FoodResponse;
import com.food.labeling.repository.FoodRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;

    public FoodServiceImpl(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Override
    public FoodResponse searchByLabels(List<String> labels, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort.Direction direction = AppConstants.ASC.equalsIgnoreCase(sortOrder)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));

        if (labels == null || labels.isEmpty()) {
            return getAllFoods(pageable);
        } else {
            return getFoodsWithAllLabels(labels, pageable);
        }
    }

    private FoodResponse getAllFoods(Pageable pageable) {
        Page<Food> foodPage = foodRepository.findAll(pageable);

        List<FoodDTO> dtos = foodPage.stream()
                .map(f -> new FoodDTO(
                        f.getFoodId(),
                        f.getFoodName(),
                        f.getLabels().stream().map(Label::getLabelName).toList()
                ))
                .toList();

        FoodResponse foodResponse = new FoodResponse();
        foodResponse.setContent(dtos);
        foodResponse.setPageNumber(foodPage.getNumber());
        foodResponse.setPageSize(foodPage.getSize());
        foodResponse.setTotalElements(foodPage.getTotalElements());
        foodResponse.setTotalPages(foodPage.getTotalPages());
        foodResponse.setLastPage(foodPage.isLast());

        return foodResponse;
    }

    private FoodResponse getFoodsWithAllLabels(List<String> labels, Pageable pageable) {
        Page<Long> foodIds = foodRepository.findFoodIdsMatchingAllLabels(labels, labels.size(), pageable);
        List<Food> foods = foodRepository.findAllById(foodIds.getContent());

        List<FoodDTO> dtos = foods.stream()
                .map(f -> new FoodDTO(
                        f.getFoodId(),
                        f.getFoodName(),
                        f.getLabels().stream().map(Label::getLabelName).toList()
                ))
                .toList();

        FoodResponse foodResponse = new FoodResponse();
        foodResponse.setContent(dtos);
        foodResponse.setPageNumber(foodIds.getNumber());
        foodResponse.setPageSize(foodIds.getSize());
        foodResponse.setTotalElements(foodIds.getTotalElements());
        foodResponse.setTotalPages(foodIds.getTotalPages());
        foodResponse.setLastPage(foodIds.isLast());

        return foodResponse;
    }
}
