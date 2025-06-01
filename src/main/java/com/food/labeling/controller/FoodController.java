package com.food.labeling.controller;

import com.food.labeling.model.Food;
import com.food.labeling.payload.FoodDTO;
import com.food.labeling.service.FoodService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/foods")
    public Page<FoodDTO> getFoods(
            @RequestParam(required = false) List<String> labels,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "foodName,asc") String[] sort
            ) {

        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        if (labels == null || labels.isEmpty()) {
            return foodService.getAllFoods(pageable);
        } else {
            return foodService.getFoodsWithAllLabels(labels, pageable);
        }
    }
}
