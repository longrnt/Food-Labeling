package com.food.labeling.repository;

import com.food.labeling.model.Label;
import com.food.labeling.payload.LabelFoodCountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {

    @Query("SELECT new com.food.labeling.payload.LabelFoodCountDTO(l.labelId, l.labelName, COUNT(f)) " +
            "FROM Label l LEFT JOIN l.foods f " +
            "GROUP BY l.labelId, l.labelName")
    List<LabelFoodCountDTO> getLabelFoodCounts();

    Optional<Label> findByLabelName(String labelName);
}