package com.food.labeling.controller;

import com.food.labeling.model.Label;
import com.food.labeling.payload.LabelCountDTO;
import com.food.labeling.repository.LabelRepository;
import com.food.labeling.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/labels")
public class LabelController {

    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping("/getAll/counts")
    public ResponseEntity<List<LabelCountDTO>> getAllLabelsWithCounts() {
        List<LabelCountDTO> labelCounts = labelService.getLabelCounts();
        return new ResponseEntity<>(labelCounts, HttpStatus.OK);
    }

    @Autowired
    private LabelRepository labelRepository;

    // Create a new label
    @PostMapping("/add")
    public ResponseEntity<Label> createLabel(@RequestBody Label label) {
        if (labelRepository.findByLabelName(label.getLabelName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Label savedLabel = labelRepository.save(label);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLabel);
    }

    // Retrieve all labels
    @GetMapping("/getAll")
    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    // Update an existing label
    @PutMapping("/update/{id}")
    public ResponseEntity<Label> updateLabel(@PathVariable Long id, @RequestBody Label labelDetails) {
        return labelRepository.findById(id).map(label -> {
            label.setLabelName(labelDetails.getLabelName());
            labelRepository.save(label);
            return ResponseEntity.ok(label);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete a label
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLabel(@PathVariable Long id) {
        return labelRepository.findById(id).map(label -> {
            labelRepository.delete(label);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
