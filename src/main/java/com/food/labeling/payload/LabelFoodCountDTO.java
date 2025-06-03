package com.food.labeling.payload;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelFoodCountDTO {
    private Long labelId;
    private String labelName;
    private Long foodCount;
}
