package com.food.labeling.controller;

import com.food.labeling.payload.LabelCountDTO;
import com.food.labeling.service.LabelService;
import com.food.labeling.service.LabelServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public/api")
public class LabelController {

    private LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping("labels")
    public ResponseEntity<List<LabelCountDTO>> getAllLabelsWithCounts() {
        List<LabelCountDTO> labelCounts = labelService.getLabelCounts();
        return new ResponseEntity<>(labelCounts, HttpStatus.OK);
    }
}
