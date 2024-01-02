package com.spotit.backend.employee.userDetails.course;

import java.time.LocalDate;

import com.spotit.backend.employee.abstraction.AbstractEntity;
import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailEntity;

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
public class Course extends AbstractUserDetailEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate finishDate;

    @Override
    public void update(AbstractEntity otherEntity) {
        if (otherEntity instanceof Course o) {
            if (o.name != null) {
                name = o.name;
            }

            if (o.finishDate != null) {
                finishDate = o.finishDate;
            }
        }
    }
}
