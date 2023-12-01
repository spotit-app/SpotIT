package com.spotit.backend.social.model;

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
public class Social extends AbstractEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String socialUrl;

    @ManyToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    @Override
    public void update(AbstractEntity otherEntity) {
        if (otherEntity instanceof Social o) {
            if (o.name != null) {
                name = o.name;
            }

            if (o.socialUrl != null) {
                socialUrl = o.socialUrl;
            }
        }
    }
}
