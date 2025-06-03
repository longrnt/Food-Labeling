package com.food.labeling.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.food.labeling.exception.APIException;
import com.food.labeling.exception.ResourceNotFoundException;
import com.food.labeling.model.Label;
import com.food.labeling.payload.LabelFoodCountDTO;
import com.food.labeling.payload.LabelDTO;
import com.food.labeling.repository.LabelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

public class LabelServiceTest {

    @Mock
    private LabelRepository labelRepository;

    @InjectMocks
    private LabelServiceImpl labelService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // getLabelFoodCounts - test get all labels with food count respectively
    @Test
    void testGetLabelFoodCount_success() {
        List<LabelFoodCountDTO> expected = List.of(
                new LabelFoodCountDTO(10L, "fruit", 3L),
                new LabelFoodCountDTO(11L, "vegetable", 5L)
        );

        when(labelRepository.getLabelFoodCounts()).thenReturn(expected);

        List<LabelFoodCountDTO> result = labelService.getLabelFoodCount();

        assertEquals(2, result.size());
        assertEquals("fruit", result.getFirst().getLabelName());
        verify(labelRepository).getLabelFoodCounts();
    }

    // createLabel - success
    @Test
    void testCreateLabel_success() {
        LabelDTO dto = new LabelDTO(null, "fruit");
        Label saved = new Label(1L, "fruit", new HashSet<>(), LocalDateTime.now(), LocalDateTime.now());

        when(labelRepository.findByLabelName("fruit")).thenReturn(Optional.empty());
        when(labelRepository.save(any(Label.class))).thenReturn(saved);

        LabelDTO result = labelService.createLabel(dto);

        assertEquals(1L, result.getLabelId());
        assertEquals("fruit", result.getLabelName());
    }

    // createLabel - name null
    @Test
    void testCreateLabel_nullName_throwsException() {
        LabelDTO dto = new LabelDTO(null, null);
        APIException ex = assertThrows(APIException.class, () -> labelService.createLabel(dto));
        assertEquals("Label name cannot be empty", ex.getMessage());
    }

    // createLabel - name empty
    @Test
    void testCreateLabel_emptyName_throwsException() {
        LabelDTO dto = new LabelDTO(null, "");
        APIException ex = assertThrows(APIException.class, () -> labelService.createLabel(dto));
        assertEquals("Label name cannot be empty", ex.getMessage());
    }

    // createLabel - duplicate
    @Test
    void testCreateLabel_duplicateName_throwsException() {
        LabelDTO dto = new LabelDTO(null, "fruit");
        when(labelRepository.findByLabelName("fruit")).thenReturn(Optional.of(new Label()));

        APIException ex = assertThrows(APIException.class, () -> labelService.createLabel(dto));
        assertTrue(ex.getMessage().contains("already exists"));
    }

    // getAllLabels
    @Test
    void testGetAllLabels_success() {
        List<Label> mockLabels = List.of(
                new Label(1L, "fruit", new HashSet<>(), LocalDateTime.now(), LocalDateTime.now()),
                new Label(2L, "vegetable", new HashSet<>(), LocalDateTime.now(), LocalDateTime.now())
        );

        when(labelRepository.findAll()).thenReturn(mockLabels);

        List<LabelDTO> result = labelService.getAllLabels();

        assertEquals(2, result.size());
        assertEquals("fruit", result.getFirst().getLabelName());
    }

    // updateLabel - success
    @Test
    void testUpdateLabel_success() {
        LabelDTO update = new LabelDTO(null, "newFruit");
        Label existing = new Label(1L, "oldFruit", new HashSet<>(), LocalDateTime.now(), LocalDateTime.now());
        Label updated = new Label(1L, "newFruit", new HashSet<>(), LocalDateTime.now(), LocalDateTime.now());

        when(labelRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(labelRepository.save(any(Label.class))).thenReturn(updated);

        LabelDTO result = labelService.updateLabel(1L, update);

        assertEquals("newFruit", result.getLabelName());
        verify(labelRepository).save(existing);
    }

    // updateLabel - empty name
    @Test
    void testUpdateLabel_emptyName_throwsException() {
        LabelDTO update = new LabelDTO(null, "");
        APIException ex = assertThrows(APIException.class, () -> labelService.updateLabel(1L, update));
        assertEquals("Label name cannot be empty", ex.getMessage());
    }

    // updateLabel - not found
    @Test
    void testUpdateLabel_labelNotFound_throwsException() {
        LabelDTO update = new LabelDTO(null, "fruit");
        when(labelRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> labelService.updateLabel(1L, update));
        assertEquals("Label", ex.getResourceName());
    }

    // deleteLabel - success
    @Test
    void testDeleteLabel_success() {
        Label label = new Label(1L, "fruit", new HashSet<>(), LocalDateTime.now(), LocalDateTime.now());

        when(labelRepository.findById(1L)).thenReturn(Optional.of(label));

        LabelDTO result = labelService.deleteLabel(1L);

        assertEquals("fruit", result.getLabelName());
        verify(labelRepository).delete(label);
    }

    // deleteLabel - not found
    @Test
    void testDeleteLabel_notFound_throwsException() {
        when(labelRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> labelService.deleteLabel(1L));
        assertEquals("Label", ex.getResourceName());
    }

    // deleteLabel - runtime error
    @Test
    void testDeleteLabel_exceptionThrown_wrappedInAPIException() {
        Label label = new Label(1L, "fruit", new HashSet<>(), LocalDateTime.now(), LocalDateTime.now());
        when(labelRepository.findById(1L)).thenReturn(Optional.of(label));
        doThrow(new RuntimeException("DB error")).when(labelRepository).delete(label);

        APIException ex = assertThrows(APIException.class, () -> labelService.deleteLabel(1L));
        assertEquals("DB error", ex.getMessage());
    }
}
