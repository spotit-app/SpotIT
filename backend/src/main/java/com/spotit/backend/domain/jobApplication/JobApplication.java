package com.spotit.backend.domain.jobApplication;

import com.spotit.backend.abstraction.AbstractEntity;
import com.spotit.backend.domain.employee.portfolio.Portfolio;
import com.spotit.backend.domain.employer.jobOffer.JobOffer;
import com.spotit.backend.domain.referenceData.applicationStatus.ApplicationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class JobApplication extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "job_offer_id", referencedColumnName = "id")
    private JobOffer jobOffer;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", referencedColumnName = "id")
    private Portfolio portfolio;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private ApplicationStatus applicationStatus;

    @Override
    public void update(AbstractEntity otherEntity) {
        throw new UnsupportedOperationException();
    }
}
