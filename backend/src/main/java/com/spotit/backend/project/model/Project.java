package com.spotit.backend.project.model;

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
public class Project extends AbstractEntity {

    @Column(nullable = false)
    private String description;

    @Column
    private String projectUrl;

    @ManyToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

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
