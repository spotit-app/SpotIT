package com.spotit.backend.employee.userAccount;

import com.spotit.backend.employee.abstraction.AbstractEntity;

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
public class UserAccount extends AbstractEntity {

    @Column(name = "auth0_id", unique = true, nullable = false)
    private String auth0Id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column
    private String phoneNumber;

    @Column
    private String profilePictureUrl;

    @Column
    private String position;

    @Column
    private String description;

    @Column
    private String cvClause;

    @Override
    public void update(AbstractEntity otherEntity) {
        if (otherEntity instanceof UserAccount o) {
            if (o.firstName != null) {
                firstName = o.firstName;
            }

            if (o.lastName != null) {
                lastName = o.lastName;
            }

            if (o.email != null) {
                email = o.email;
            }

            if (o.phoneNumber != null) {
                phoneNumber = o.phoneNumber;
            }

            if (o.profilePictureUrl != null) {
                profilePictureUrl = o.profilePictureUrl;
            }

            if (o.position != null) {
                position = o.position;
            }

            if (o.description != null) {
                description = o.description;
            }

            if (o.cvClause != null) {
                cvClause = o.cvClause;
            }
        }
    }
}
