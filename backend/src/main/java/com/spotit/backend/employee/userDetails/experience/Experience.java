package com.spotit.backend.employee.userDetails.experience;

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
public class Experience extends AbstractUserDetailEntity {

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Override
    public void update(AbstractEntity otherEntity) {
        if (otherEntity instanceof Experience o) {
            if (o.companyName != null) {
                companyName = o.companyName;
            }

            if (o.position != null) {
                position = o.position;
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
