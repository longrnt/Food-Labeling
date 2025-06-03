package com.food.labeling.repository;

import com.food.labeling.model.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    @Query("""
        SELECT f
        FROM Food f
        JOIN f.labels l
        WHERE l.labelName IN :labels
        GROUP BY f
        HAVING COUNT(DISTINCT l.labelName) = :labelCount
    """)
    Page<Food> findFoodIdsMatchingAllLabels(@Param("labels") List<String> labels,
                                            @Param("labelCount") long labelCount,
                                            Pageable pageable);
}
