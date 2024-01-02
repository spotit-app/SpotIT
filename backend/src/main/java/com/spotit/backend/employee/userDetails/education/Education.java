package com.spotit.backend.employee.userDetails.education;

import static jakarta.persistence.CascadeType.PERSIST;

import java.time.LocalDate;

import com.spotit.backend.employee.abstraction.AbstractEntity;
import com.spotit.backend.employee.referenceData.educationLevel.EducationLevel;
import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Education extends AbstractUserDetailEntity {

    @Column(nullable = false)
    private String schoolName;

    @Column
    private String faculty;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = "education_level_id")
    private EducationLevel educationLevel;

    @Override
    public void update(AbstractEntity otherEntity) {
        if (otherEntity instanceof Education o) {
            if (o.schoolName != null) {
                schoolName = o.schoolName;
            }

            if (o.faculty != null) {
                faculty = o.faculty;
            }

            if (o.startDate != null) {
                startDate = o.startDate;
            }

            if (o.endDate != null) {
                endDate = o.endDate;
            }
        }
    }
}
