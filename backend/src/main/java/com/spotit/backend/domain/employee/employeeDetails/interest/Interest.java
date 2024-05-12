package com.spotit.backend.domain.employee.employeeDetails.interest;

import com.spotit.backend.abstraction.AbstractEntity;
import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailEntity;

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
public class Interest extends AbstractUserDetailEntity {

    @Column(nullable = false)
    private String name;

    @Override
    public void update(AbstractEntity otherEntity) {
        if (otherEntity instanceof Interest o) {
            if (o.name != null) {
                name = o.name;
            }
        }
    }
}
