package com.food.labeling.controller;

import com.food.labeling.config.AppConstants;
import com.food.labeling.payload.FoodResponse;
import com.food.labeling.service.FoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/foods")
@Slf4j
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/getByLabels")
    public ResponseEntity<FoodResponse> getFoods(
            @RequestParam(required = false) List<String> labels,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.FOOD_NAME, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.ASC, required = false) String sortOrder
    ) {
        log.info("Received request to get foods");
        FoodResponse foodResponse = foodService.searchByLabels(labels, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(foodResponse, HttpStatus.OK);
    }
}
