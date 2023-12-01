package com.spotit.backend.education.model;

import static jakarta.persistence.GenerationType.AUTO;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationLevel {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private boolean custom;

    @OneToMany(mappedBy = "educationLevel")
    private List<Education> educations;
}
