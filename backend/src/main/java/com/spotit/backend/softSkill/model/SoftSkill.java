package com.spotit.backend.softSkill.model;

import static jakarta.persistence.CascadeType.PERSIST;

import com.spotit.backend.abstraction.model.AbstractEntity;
import com.spotit.backend.userAccount.model.UserAccount;

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
public class SoftSkill extends AbstractEntity {

    @Column(nullable = false)
    private Integer skillLevel;

    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = "soft_skill_name_id")
    private SoftSkillName softSkillName;

    @ManyToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    @Override
    public void update(AbstractEntity otherEntity) {
        if (otherEntity instanceof SoftSkill o) {
            if (o.skillLevel != null) {
                skillLevel = o.skillLevel;
            }
        }
    }
}
