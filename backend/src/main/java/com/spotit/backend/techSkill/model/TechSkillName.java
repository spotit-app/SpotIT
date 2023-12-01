package com.spotit.backend.techSkill.model;

import java.util.List;

import com.spotit.backend.abstraction.model.AbstractEntity;

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
public class TechSkillName extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String logoUrl;

    @Column(nullable = false)
    private Boolean custom;

    @OneToMany(mappedBy = "techSkillName")
    private List<TechSkill> techSkills;

    @Override
    public void update(AbstractEntity otherEntity) {
        if (otherEntity instanceof TechSkillName o) {
            if (o.name != null) {
                name = o.name;
            }

            if (o.logoUrl != null) {
                logoUrl = o.logoUrl;
            }

            if (o.custom != null) {
                custom = o.custom;
            }
        }
    }
}
