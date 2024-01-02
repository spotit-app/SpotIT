package com.spotit.backend.employee.userDetails.project;

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
public class Project extends AbstractUserDetailEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column
    private String projectUrl;

    @Override
    public void update(AbstractEntity otherEntity) {
        if (otherEntity instanceof Project o) {
            if (o.description != null) {
                description = o.description;
            }

            if (o.projectUrl != null) {
                projectUrl = o.projectUrl;
            }
        }
    }
}
