package com.food.labeling.service;

import com.food.labeling.exception.APIException;
import com.food.labeling.exception.ResourceNotFoundException;
import com.food.labeling.model.Label;
import com.food.labeling.payload.LabelFoodCountDTO;
import com.food.labeling.payload.LabelDTO;
import com.food.labeling.repository.LabelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    public LabelServiceImpl(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    @Override
    public List<LabelFoodCountDTO> getLabelFoodCount() {
        List<LabelFoodCountDTO> labelFoodCountList = labelRepository.getLabelFoodCounts();

        //Since labelFoodCountList is un-sortable, we have to clone it to another list to sort it.
        List<LabelFoodCountDTO> resultList = new ArrayList<>(labelFoodCountList);

        //Sort result list so that it will be easier for users to look up labels in Food Management page
        resultList.sort((a, b)
                -> a.getLabelName().compareTo(b.getLabelName()));

        return resultList;
    }

    @Override
    public LabelDTO createLabel(LabelDTO labelDTO) {
        if (labelDTO.getLabelName() == null || labelDTO.getLabelName().isEmpty()) {
            log.error("Label name cannot be empty");
            throw new APIException("Label name cannot be empty");
        }
        if (labelRepository.findByLabelName(labelDTO.getLabelName()).isPresent()) {
            log.error("Label with name {} already exists", labelDTO.getLabelName());
            throw new APIException("The label " + labelDTO.getLabelName() + " already exists.");
        }

        Label label = new Label();
        label.setLabelName(labelDTO.getLabelName());

        Label savedLabel = labelRepository.save(label);
        return new LabelDTO(savedLabel.getLabelId(), savedLabel.getLabelName());
    }

    @Override
    public List<LabelDTO> getAllLabels() {
        List<Label> labelList = labelRepository.findAll();

        return labelList.stream()
                .map(l -> new LabelDTO(l.getLabelId(), l.getLabelName()))
                .toList();
    }

    @Override
    public LabelDTO updateLabel(Long labelId, LabelDTO labelDetails) {

        if (labelDetails.getLabelName() == null || labelDetails.getLabelName().isEmpty()) {
            log.error("Label name cannot be empty");
            throw new APIException("Label name cannot be empty");
        }

        Optional<Label> labelOpt = labelRepository.findById(labelId);
        if (labelOpt.isEmpty()) {
            log.error("Label with id {} does not exist", labelId);
            throw new ResourceNotFoundException("Label", "labelId", labelId);
        }

        Label labelData = labelOpt.get();
        labelData.setLabelName(labelDetails.getLabelName());
        Label updatedLabel = labelRepository.save(labelData);

        return new LabelDTO(updatedLabel.getLabelId(), updatedLabel.getLabelName());
    }

    @Override
    public LabelDTO deleteLabel(Long labelId) {
        Optional<Label> labelOpt = labelRepository.findById(labelId);
        if (labelOpt.isEmpty()) {
            log.error("Label with id {} does not exist", labelId);
            throw new ResourceNotFoundException("Label", "labelId", labelId);
        }

        try {
            Label labelData = labelOpt.get();
            labelRepository.delete(labelData);
            return new LabelDTO(labelData.getLabelId(), labelData.getLabelName());

        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            throw new APIException(e.getMessage());
        }
    }
}
