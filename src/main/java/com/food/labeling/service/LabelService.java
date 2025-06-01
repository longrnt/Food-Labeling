package com.food.labeling.service;

import com.food.labeling.payload.LabelCountDTO;

import java.util.List;

public interface LabelService {
    List<LabelCountDTO> getLabelCounts();
}
