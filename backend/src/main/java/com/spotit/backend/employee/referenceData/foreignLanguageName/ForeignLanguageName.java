package com.spotit.backend.employee.referenceData.foreignLanguageName;

import java.util.List;

import com.spotit.backend.employee.abstraction.AbstractEntity;
import com.spotit.backend.employee.userDetails.foreignLanguage.ForeignLanguage;

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
public class ForeignLanguageName extends AbstractEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String flagUrl;

    @OneToMany(mappedBy = "foreignLanguageName")
    private List<ForeignLanguage> foreignLanguages;

    @Override
    public void update(AbstractEntity otherEntity) {
        if (otherEntity instanceof ForeignLanguageName o) {
            if (o.name != null) {
                name = o.name;
            }

            if (flagUrl != null) {
                flagUrl = o.flagUrl;
            }
        }
    }
}
