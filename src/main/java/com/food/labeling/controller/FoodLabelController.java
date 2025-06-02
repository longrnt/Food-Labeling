package com.food.labeling.controller;

import com.food.labeling.model.Food;
import com.food.labeling.model.Label;
import com.food.labeling.repository.FoodRepository;
import com.food.labeling.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/associate/foods")
public class FoodLabelController {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private LabelRepository labelRepository;

    @PostMapping("/{foodId}/labels/{labelId}")
    public ResponseEntity<?> assignLabelToFood(@PathVariable Long foodId, @PathVariable Long labelId) {
        Optional<Food> foodOpt = foodRepository.findById(foodId);
        Optional<Label> labelOpt = labelRepository.findById(labelId);

        if (foodOpt.isPresent() && labelOpt.isPresent()) {
            Food food = foodOpt.get();
            Label label = labelOpt.get();
            food.getLabels().add(label);
            foodRepository.save(food);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{foodId}/labels/{labelId}")
    public ResponseEntity<?> unassignLabelFromFood(@PathVariable Long foodId, @PathVariable Long labelId) {
        Optional<Food> foodOpt = foodRepository.findById(foodId);
        Optional<Label> labelOpt = labelRepository.findById(labelId);

        if (foodOpt.isPresent() && labelOpt.isPresent()) {
            Food food = foodOpt.get();
            Label label = labelOpt.get();
            food.getLabels().remove(label);
            foodRepository.save(food);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}