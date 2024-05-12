package com.spotit.backend.domain.referenceData.educationLevel;

import java.util.List;

import com.spotit.backend.abstraction.AbstractEntity;
import com.spotit.backend.domain.employee.employeeDetails.education.Education;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
public class EducationLevel extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Boolean custom;

    @OneToMany(mappedBy = "educationLevel")
    private List<Education> educations;

    @Override
    public void update(AbstractEntity otherEntity) {
        if (otherEntity instanceof EducationLevel o) {
            if (o.name != null) {
                name = o.name;
            }

            if (o.custom != null) {
                custom = o.custom;
            }
        }
    }
}
