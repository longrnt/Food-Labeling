package com.food.labeling.service;

import com.food.labeling.config.AppConstants;
import com.food.labeling.exception.APIException;
import com.food.labeling.exception.ResourceNotFoundException;
import com.food.labeling.model.Food;
import com.food.labeling.model.Label;
import com.food.labeling.payload.FoodDTO;
import com.food.labeling.payload.FoodResponse;
import com.food.labeling.repository.FoodRepository;
import com.food.labeling.repository.LabelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;
    private final LabelRepository labelRepository;

    public FoodServiceImpl(FoodRepository foodRepository, LabelRepository labelRepository) {
        this.foodRepository = foodRepository;
        this.labelRepository = labelRepository;
    }

    @Override
    public FoodResponse searchFoodsByLabels(List<String> labels, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort.Direction direction = AppConstants.ASC.equalsIgnoreCase(sortOrder)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));

        Page<Food> foodPage = null;

        if (labels == null || labels.isEmpty()) {
            foodPage = foodRepository.findAll(pageable);
        } else {
            foodPage = foodRepository.getFoodsMatchingAllLabels(labels, labels.size(), pageable);
        }

        if (foodPage == null) {
            log.error("No food found");
            throw new APIException("No food found");
        }

        List<FoodDTO> foodDTOList = foodPage.stream()
                .map(f -> new FoodDTO(
                        f.getFoodId(),
                        f.getFoodName(),
                        f.getLabels().stream().map(Label::getLabelName).collect(Collectors.toSet())
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

    @Override
    public void assignLabelToFood(Long foodId, Long labelId) {
        Optional<Food> foodOpt = foodRepository.findById(foodId);
        Optional<Label> labelOpt = labelRepository.findById(labelId);

        if (foodOpt.isEmpty()) {
            log.error("Food with id {} does not exist", foodId);
            throw new ResourceNotFoundException("Food", "foodId", foodId);
        }
        if (labelOpt.isEmpty()) {
            log.error("Label with id {} does not exist", labelId);
            throw new ResourceNotFoundException("Label", "labelId", labelId);
        }

        Food food = foodOpt.get();
        Label label = labelOpt.get();
        food.getLabels().add(label);
        foodRepository.save(food);
    }

    @Override
    public void unassignLabelFromFood(Long foodId, Long labelId) {
        Optional<Food> foodOpt = foodRepository.findById(foodId);
        Optional<Label> labelOpt = labelRepository.findById(labelId);

        if (foodOpt.isEmpty()) {
            log.error("Food with id {} does not exist", foodId);
            throw new ResourceNotFoundException("Food", "foodId", foodId);
        }
        if (labelOpt.isEmpty()) {
            log.error("Label with id {} does not exist", labelId);
            throw new ResourceNotFoundException("Label", "labelId", labelId);
        }

        Food food = foodOpt.get();
        Label label = labelOpt.get();
        food.getLabels().remove(label);
        foodRepository.save(food);
    }

}
