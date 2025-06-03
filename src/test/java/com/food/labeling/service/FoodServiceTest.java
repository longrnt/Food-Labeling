package com.food.labeling.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.food.labeling.exception.APIException;
import com.food.labeling.exception.ResourceNotFoundException;
import com.food.labeling.model.Food;
import com.food.labeling.model.Label;
import com.food.labeling.payload.FoodResponse;
import com.food.labeling.repository.FoodRepository;
import com.food.labeling.repository.LabelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

public class FoodServiceTest {

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private LabelRepository labelRepository;

    @InjectMocks
    private FoodServiceImpl foodService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchFoodsByLabels_emptyLabels_returnsAllFoods() {
        List<String> labels = Collections.emptyList();
        int page = 0;
        int size = 2;
        String sortBy = "foodName";
        String sortOrder = "asc";

        Food food1 = new Food(1L, "Apple", new HashSet<>(), LocalDateTime.now() , LocalDateTime.now());
        Food food2 = new Food(2L, "Banana", new HashSet<>(), LocalDateTime.now() , LocalDateTime.now());
        Page<Food> mockPage = new PageImpl<>(List.of(food1, food2));

        when(foodRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        FoodResponse response = foodService.searchFoodsByLabels(labels, page, size, sortBy, sortOrder);

        assertEquals(2, response.getContent().size());
        verify(foodRepository).findAll(any(Pageable.class));
    }

    @Test
    void testSearchFoodsByLabels_withLabels_returnsFilteredFoods() {
        List<String> labels = List.of("fruit");
        int page = 0;
        int size = 1;
        String sortBy = "foodName";
        String sortOrder = "desc";

        Label label = new Label(1L, "fruit", new HashSet<>(), LocalDateTime.now(), LocalDateTime.now());
        Food food = new Food(1L, "Orange", Set.of(label), LocalDateTime.now(), LocalDateTime.now());
        Page<Food> mockPage = new PageImpl<>(List.of(food));

        when(foodRepository.getFoodsMatchingAllLabels(eq(labels), eq(1L), any(Pageable.class)))
                .thenReturn(mockPage);

        FoodResponse response = foodService.searchFoodsByLabels(labels, page, size, sortBy, sortOrder);

        assertEquals(1, response.getContent().size());
        assertEquals("Orange", response.getContent().getFirst().getFoodName());
    }

    @Test
    void testSearchFoodsByLabels_nullResult_throwsException() {
        when(foodRepository.findAll(any(Pageable.class))).thenReturn(null);

        assertThrows(APIException.class,
                () -> foodService.searchFoodsByLabels(null, 0, 10, "foodId", "asc"));
    }

    @Test
    void testAssignLabelToFood_success() {
        Long foodId = 1L, labelId = 2L;
        Food food = new Food(foodId, "Tomato", new HashSet<>(), LocalDateTime.now(), LocalDateTime.now());
        Label label = new Label(labelId, "vegetable", new HashSet<>(), LocalDateTime.now(), LocalDateTime.now());

        when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));
        when(labelRepository.findById(labelId)).thenReturn(Optional.of(label));

        foodService.assignLabelToFood(foodId, labelId);

        assertTrue(food.getLabels().contains(label));
        verify(foodRepository).save(food);
    }

    @Test
    void testAssignLabelToFood_missingFood_throwsException() {
        when(foodRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> foodService.assignLabelToFood(1L, 2L));
        assertEquals("Food", ex.getResourceName());
    }

    @Test
    void testAssignLabelToFood_missingLabel_throwsException() {
        when(foodRepository.findById(1L)).thenReturn(Optional.of(new Food()));
        when(labelRepository.findById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> foodService.assignLabelToFood(1L, 2L));
        assertEquals("Label", ex.getResourceName());
    }

    @Test
    void testUnassignLabelFromFood_success() {
        Long foodId = 1L, labelId = 2L;
        Label label = new Label(labelId, "fruit", new HashSet<>(), LocalDateTime.now(), LocalDateTime.now());
        Food food = new Food(foodId, "Apple", new HashSet<>(Set.of(label)), LocalDateTime.now(), LocalDateTime.now());

        when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));
        when(labelRepository.findById(labelId)).thenReturn(Optional.of(label));

        // Before unassign, food "Apple" still associate to label "fruit"
        assertTrue(food.getLabels().contains(label));

        foodService.unassignLabelFromFood(foodId, labelId);

        // After unassign, food "Apple" no longer associate to label "fruit"
        assertFalse(food.getLabels().contains(label));
        verify(foodRepository).save(food);
    }

    @Test
    void testUnassignLabelFromFood_missingFood_throwsException() {
        when(foodRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> foodService.unassignLabelFromFood(1L, 2L));
        assertEquals("Food", ex.getResourceName());
    }

    @Test
    void testUnassignLabelFromFood_missingLabel_throwsException() {
        when(foodRepository.findById(1L)).thenReturn(Optional.of(new Food()));
        when(labelRepository.findById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> foodService.unassignLabelFromFood(1L, 2L));
        assertEquals("Label", ex.getResourceName());
    }
}
