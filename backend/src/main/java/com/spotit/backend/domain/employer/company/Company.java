package com.spotit.backend.domain.employer.company;

import com.spotit.backend.abstraction.AbstractEntity;
import com.spotit.backend.domain.employer.address.Address;
import com.spotit.backend.domain.userAccount.UserAccount;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class Company extends AbstractEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nip;

    @Column(unique = true)
    private String regon;

    @Column
    private String websiteUrl;

    @Column
    private String logoUrl;

    @ManyToOne
    @JoinColumn(name = "user_account_id", referencedColumnName = "id")
    private UserAccount userAccount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Override
    public void update(AbstractEntity otherEntity) {
        if (otherEntity instanceof Company o) {

            if (o.name != null) {
                name = o.name;
            }

            if (o.nip != null) {
                nip = o.nip;
            }

            if (o.regon != null) {
                regon = o.regon;
            }

            if (o.websiteUrl != null) {
                websiteUrl = o.websiteUrl;
            }

            if (o.logoUrl != null) {
                logoUrl = o.logoUrl;
            }
        }
    }
}
