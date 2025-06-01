package com.food.labeling.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "label")
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "label_id")
    private Long labelId;

    @Column(name = "label_name")
    private String labelName;

    @ManyToMany(mappedBy = "labels")
    private Set<Food> foods = new HashSet<>();

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
