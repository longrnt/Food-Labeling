package com.food.labeling.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FoodDTO {
    private Long foodId;
    private String foodName;
    private List<String> labels;
}
