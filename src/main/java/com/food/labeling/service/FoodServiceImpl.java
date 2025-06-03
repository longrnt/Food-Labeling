package com.food.labeling.service;

import com.food.labeling.config.AppConstants;
import com.food.labeling.model.Food;
import com.food.labeling.model.Label;
import com.food.labeling.payload.FoodDTO;
import com.food.labeling.payload.FoodResponse;
import com.food.labeling.repository.FoodRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

        Page<Food> foodPage = null;

        if (labels == null || labels.isEmpty()) {
            foodPage = foodRepository.findAll(pageable);
        } else {
            foodPage = foodRepository.findFoodIdsMatchingAllLabels(labels, labels.size(), pageable);
        }

        if (foodPage == null) {
            throw new RuntimeException("No food found");
        }

        List<FoodDTO> foodDTOList = foodPage.stream()
                .map(f -> new FoodDTO(
                        f.getFoodId(),
                        f.getFoodName(),
                        f.getLabels().stream().map(Label::getLabelName).toList()
                ))
                .toList();

        FoodResponse foodResponse = new FoodResponse();
        foodResponse.setContent(foodDTOList);
        foodResponse.setPageNumber(foodPage.getNumber());
        foodResponse.setPageSize(foodPage.getSize());
        foodResponse.setTotalElements(foodPage.getTotalElements());
        foodResponse.setTotalPages(foodPage.getTotalPages());
        foodResponse.setLastPage(foodPage.isLast());

        return foodResponse;
    }

}
