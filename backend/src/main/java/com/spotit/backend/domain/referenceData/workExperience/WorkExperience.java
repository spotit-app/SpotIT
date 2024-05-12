package com.spotit.backend.domain.referenceData.workExperience;

import com.spotit.backend.abstraction.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class WorkExperience extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Override
    public void update(AbstractEntity otherEntity) {
        if (otherEntity instanceof WorkExperience o) {

            if (o.name != null) {
                name = o.name;
            }
        }
    }
}
