package com.food.labeling.controller;

import com.food.labeling.config.AppConstants;
import com.food.labeling.payload.FoodDTO;
import com.food.labeling.payload.FoodResponse;
import com.food.labeling.service.FoodService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public/api")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

//    @GetMapping("/foods")
//    public Page<FoodDTO> getFoods(
//            @RequestParam(required = false) List<String> labels,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "foodName,asc") String[] sort
//            ) {
//
//        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
//        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
//
//        if (labels == null || labels.isEmpty()) {
//            return foodService.getAllFoods(pageable);
//        } else {
//            return foodService.getFoodsWithAllLabels(labels, pageable);
//        }
//    }

    @GetMapping("/foods")
    public ResponseEntity<FoodResponse> getFoods(
            @RequestParam(required = false) List<String> labels,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.FOOD_NAME, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.ASC, required = false) String sortOrder
    ) {

        FoodResponse foodResponse = foodService.searchByLabels(labels, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(foodResponse, HttpStatus.OK);
    }
}
