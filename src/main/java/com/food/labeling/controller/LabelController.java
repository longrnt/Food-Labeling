package com.food.labeling.controller;

import com.food.labeling.payload.LabelFoodCountDTO;
import com.food.labeling.payload.LabelDTO;
import com.food.labeling.service.LabelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/labels")
@Slf4j
public class LabelController {

    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping("/getAll/counts")
    public ResponseEntity<List<LabelFoodCountDTO>> getAllLabelsWithFoodCount() {
        log.info("Received GET /labels/getAll/counts request");
        List<LabelFoodCountDTO> labelCounts = labelService.getLabelFoodCount();
        return new ResponseEntity<>(labelCounts, HttpStatus.OK);
    }

    // Create a new label
    @PostMapping("/add")
    public ResponseEntity<LabelDTO> createLabel(@RequestBody LabelDTO labelDTO) {
        log.info("Received POST /labels/add with body: {}", labelDTO);
        LabelDTO savedLabelDTO = labelService.createLabel(labelDTO);
        return new ResponseEntity<>(savedLabelDTO, HttpStatus.CREATED);
    }

    // Retrieve all labels
    @GetMapping("/getAll")
    public ResponseEntity<List<LabelDTO>> getAllLabels() {
        log.info("Received GET /labels/getAll request");
        List<LabelDTO> labelDTOList = labelService.getAllLabels();
        return new ResponseEntity<>(labelDTOList, HttpStatus.OK);
    }

    // Update an existing label
    @PutMapping("/update/{labelId}")
    public ResponseEntity<LabelDTO> updateLabel(@PathVariable Long labelId, @RequestBody LabelDTO labelDetails) {
        log.info("Received PUT /labels/update/{} with body: {}", labelId, labelDetails);
        LabelDTO updatedLabel = labelService.updateLabel(labelId, labelDetails);
        return new ResponseEntity<>(updatedLabel, HttpStatus.OK);
    }

    // Delete a label
    @DeleteMapping("/delete/{labelId}")
    public ResponseEntity<LabelDTO> deleteLabel(@PathVariable Long labelId) {
        log.info("Received DELETE /labels/delete/{}", labelId);
        LabelDTO deletedLabel = labelService.deleteLabel(labelId);
        return new ResponseEntity<>(deletedLabel, HttpStatus.OK);
    }
}
