package com.spotit.backend.foreignLanguage.model;

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
public class ForeignLanguage extends AbstractEntity {

    @Column(nullable = false)
    private String languageLevel;

    @ManyToOne
    @JoinColumn(name = "foreign_language_name_id")
    private ForeignLanguageName foreignLanguageName;

    @ManyToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    @Override
    public void update(AbstractEntity otherEntity) {
        if (otherEntity instanceof ForeignLanguage o) {
            if (o.languageLevel != null) {
                languageLevel = o.languageLevel;
            }
        }
    }
}
