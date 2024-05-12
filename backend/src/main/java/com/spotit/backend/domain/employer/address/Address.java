package com.spotit.backend.domain.employer.address;

import com.spotit.backend.abstraction.AbstractEntity;

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
public class Address extends AbstractEntity {

    @Column(nullable = false)
    private String country;

    @Column
    private String zipCode;

    @Column
    private String city;

    @Column(nullable = false)
    private String street;

    @Override
    public void update(AbstractEntity otherEntity) {
        if (otherEntity instanceof Address o) {

            if (o.country != null) {
                country = o.country;
            }

            if (o.zipCode != null) {
                zipCode = o.zipCode;
            }

            if (o.city != null) {
                city = o.city;
            }

            if (o.street != null) {
                street = o.street;
            }
        }
    }
}
