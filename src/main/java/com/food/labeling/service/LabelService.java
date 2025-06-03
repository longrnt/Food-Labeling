package com.food.labeling.service;

import com.food.labeling.payload.LabelCountDTO;
import com.food.labeling.payload.LabelDTO;

import java.util.List;

public interface LabelService {
    List<LabelCountDTO> getLabelCounts();

    LabelDTO createLabel(LabelDTO labelDTO);

    List<LabelDTO> getAllLabels();

    LabelDTO updateLabel(Long labelId, LabelDTO labelDetails);

    LabelDTO deleteLabel(Long labelId);
}
