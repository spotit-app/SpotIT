package com.spotit.backend.domain.employee.employeeDetails.foreignLanguage;

import com.spotit.backend.abstraction.AbstractEntity;
import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailEntity;
import com.spotit.backend.domain.referenceData.foreignLanguageName.ForeignLanguageName;

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
public class ForeignLanguage extends AbstractUserDetailEntity {

    @Column(nullable = false)
    private String languageLevel;

    @ManyToOne
    @JoinColumn(name = "foreign_language_name_id")
    private ForeignLanguageName foreignLanguageName;

    @Override
    public void update(AbstractEntity otherEntity) {
        if (otherEntity instanceof ForeignLanguage o) {
            if (o.languageLevel != null) {
                languageLevel = o.languageLevel;
            }
        }
    }
}
