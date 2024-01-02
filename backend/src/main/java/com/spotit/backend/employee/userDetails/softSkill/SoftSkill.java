package com.spotit.backend.employee.userDetails.softSkill;

import static jakarta.persistence.CascadeType.PERSIST;

import com.spotit.backend.employee.abstraction.AbstractEntity;
import com.spotit.backend.employee.referenceData.softSkillName.SoftSkillName;
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
public class SoftSkill extends AbstractUserDetailEntity {

    @Column(nullable = false)
    private Integer skillLevel;

    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = "soft_skill_name_id")
    private SoftSkillName softSkillName;

    @Override
    public void update(AbstractEntity otherEntity) {
        if (otherEntity instanceof SoftSkill o) {
            if (o.skillLevel != null) {
                skillLevel = o.skillLevel;
            }
        }
    }
}
