package com.food.labeling.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelCountDTO {
    private String label;
    private Long count;
}
