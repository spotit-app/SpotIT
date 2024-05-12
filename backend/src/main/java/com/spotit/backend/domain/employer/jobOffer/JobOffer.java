package com.spotit.backend.domain.employer.jobOffer;

import java.time.LocalDate;
import java.util.List;

import com.spotit.backend.abstraction.AbstractEntity;
import com.spotit.backend.domain.employer.company.Company;
import com.spotit.backend.domain.referenceData.foreignLanguageName.ForeignLanguageName;
import com.spotit.backend.domain.referenceData.softSkillName.SoftSkillName;
import com.spotit.backend.domain.referenceData.techSkillName.TechSkillName;
import com.spotit.backend.domain.referenceData.workExperience.WorkExperience;
import com.spotit.backend.domain.referenceData.workMode.WorkMode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
public class JobOffer extends AbstractEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer minSalary;

    @Column
    private Integer maxSalary;

    @Column
    private String benefits;

    @Column(nullable = false)
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "work_experience_id", referencedColumnName = "id")
    private WorkExperience workExperience;

    @ManyToMany
    @JoinTable(name = "job_offer_tech_skill_name", joinColumns = @JoinColumn(name = "job_offer_id"), inverseJoinColumns = @JoinColumn(name = "tech_skill_name_id"))
    private List<TechSkillName> techSkillNames;

    @ManyToMany
    @JoinTable(name = "job_offer_soft_skill_name", joinColumns = @JoinColumn(name = "job_offer_id"), inverseJoinColumns = @JoinColumn(name = "soft_skill_name_id"))
    private List<SoftSkillName> softSkillNames;

    @ManyToMany
    @JoinTable(name = "job_offer_foreign_language_name", joinColumns = @JoinColumn(name = "job_offer_id"), inverseJoinColumns = @JoinColumn(name = "foreign_language_name_id"))
    private List<ForeignLanguageName> foreignLanguageNames;

    @ManyToMany
    @JoinTable(name = "job_offer_work_mode", joinColumns = @JoinColumn(name = "job_offer_id"), inverseJoinColumns = @JoinColumn(name = "work_mode_id"))
    private List<WorkMode> workModes;

    @Override
    public void update(AbstractEntity otherEntity) {
        if (otherEntity instanceof JobOffer o) {

            if (o.name != null) {
                name = o.name;
            }

            if (o.position != null) {
                position = o.position;
            }

            if (o.description != null) {
                description = o.description;
            }

            if (o.minSalary != null) {
                minSalary = o.minSalary;
            }

            if (o.maxSalary != null) {
                maxSalary = o.maxSalary;
            }

            if (o.benefits != null) {
                benefits = o.benefits;
            }

            if (o.dueDate != null) {
                dueDate = o.dueDate;
            }
        }
    }
}
