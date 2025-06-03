package com.food.labeling.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "label")
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "label_id")
    private Long labelId;

    @Column(name = "label_name", unique = true, nullable = false)
    @Size(min = 1, message = "Label name must have at least 1 character.")
    private String labelName;

    @ManyToMany(mappedBy = "labels")
    @ToString.Exclude
    @JsonIgnore
    private Set<Food> foods = new HashSet<>();

    @UpdateTimestamp
    @JsonIgnore
    private LocalDateTime updatedAt;

    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime createdAt;
}
