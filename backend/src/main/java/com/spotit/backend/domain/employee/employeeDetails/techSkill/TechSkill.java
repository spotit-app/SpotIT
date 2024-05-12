package com.spotit.backend.domain.employee.employeeDetails.techSkill;

import static jakarta.persistence.CascadeType.PERSIST;

import com.spotit.backend.abstraction.AbstractEntity;
import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailEntity;
import com.spotit.backend.domain.referenceData.techSkillName.TechSkillName;
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
public class TechSkill extends AbstractUserDetailEntity {

    @Column(nullable = false)
    private Integer skillLevel;

    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = "tech_skill_name_id")
    private TechSkillName techSkillName;

    @Override
    public void update(AbstractEntity otherEntity) {
        if (otherEntity instanceof TechSkill o) {
            if (o.skillLevel != null) {
                skillLevel = o.skillLevel;
            }
        }
    }
}
