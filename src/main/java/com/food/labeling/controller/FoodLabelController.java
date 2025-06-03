package com.food.labeling.controller;

import com.food.labeling.config.AppConstants;
import com.food.labeling.model.Food;
import com.food.labeling.model.Label;
import com.food.labeling.payload.FoodResponse;
import com.food.labeling.repository.FoodRepository;
import com.food.labeling.repository.LabelRepository;
import com.food.labeling.service.FoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/foods")
@Slf4j
public class FoodLabelController {

    private final FoodService foodService;

    public FoodLabelController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/getByLabels")
    public ResponseEntity<FoodResponse> getFoodsByLabels(
            @RequestParam(required = false) List<String> labels,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.FOOD_NAME, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.ASC, required = false) String sortOrder
    ) {
        log.info("Received request to get foods by labels");
        FoodResponse foodResponse = foodService.searchByLabels(labels, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(foodResponse, HttpStatus.OK);
    }

    @PostMapping("/{foodId}/labels/{labelId}")
    public ResponseEntity<?> assignLabelToFood(@PathVariable Long foodId, @PathVariable Long labelId) {
        foodService.assignLabelToFood(foodId, labelId);
        return ResponseEntity.ok().build();

//        Optional<Food> foodOpt = foodRepository.findById(foodId);
//        Optional<Label> labelOpt = labelRepository.findById(labelId);
//
//        if (foodOpt.isPresent() && labelOpt.isPresent()) {
//            Food food = foodOpt.get();
//            Label label = labelOpt.get();
//            food.getLabels().add(label);
//            foodRepository.save(food);
//            return ResponseEntity.ok().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
    }

    @DeleteMapping("/{foodId}/labels/{labelId}")
    public ResponseEntity<?> unassignLabelFromFood(@PathVariable Long foodId, @PathVariable Long labelId) {

        foodService.unassignLabelFromFood(foodId, labelId);
        return ResponseEntity.ok().build();

//        Optional<Food> foodOpt = foodRepository.findById(foodId);
//        Optional<Label> labelOpt = labelRepository.findById(labelId);
//
//        if (foodOpt.isPresent() && labelOpt.isPresent()) {
//            Food food = foodOpt.get();
//            Label label = labelOpt.get();
//            food.getLabels().remove(label);
//            foodRepository.save(food);
//            return ResponseEntity.ok().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
    }
}