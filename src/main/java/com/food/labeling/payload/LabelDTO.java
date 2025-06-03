package com.food.labeling.payload;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LabelDTO {
    private Long labelId;
    private String labelName;
}
