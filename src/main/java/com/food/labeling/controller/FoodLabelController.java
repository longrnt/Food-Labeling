package com.food.labeling.controller;

import com.food.labeling.config.AppConstants;
import com.food.labeling.payload.FoodResponse;
import com.food.labeling.service.FoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        log.info("Received GET /foods/getByLabels with params: labels={}, pageNumber={}, pageSize={}, sortBy={}, sortOrder={}",
                labels, pageNumber, pageSize, sortBy, sortOrder);
        FoodResponse foodResponse = foodService.searchFoodsByLabels(labels, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(foodResponse, HttpStatus.OK);
    }

    @PostMapping("/{foodId}/labels/{labelId}")
    public ResponseEntity<?> assignLabelToFood(@PathVariable Long foodId, @PathVariable Long labelId) {
        log.info("Received POST /foods/{}/labels/{} to assign label", foodId, labelId);
        foodService.assignLabelToFood(foodId, labelId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{foodId}/labels/{labelId}")
    public ResponseEntity<?> unassignLabelFromFood(@PathVariable Long foodId, @PathVariable Long labelId) {
        log.info("Received DELETE /foods/{}/labels/{} to unassign label", foodId, labelId);
        foodService.unassignLabelFromFood(foodId, labelId);
        return ResponseEntity.ok().build();
    }
}