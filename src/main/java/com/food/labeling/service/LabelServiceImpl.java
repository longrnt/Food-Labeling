package com.food.labeling.service;

import com.food.labeling.payload.LabelCountDTO;
import com.food.labeling.repository.LabelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    public LabelServiceImpl(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    @Override
    public List<LabelCountDTO> getLabelCounts() {
        return labelRepository.getLabelCounts();
    }
}
