package com.spotit.backend.domain.employer.jobOffer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.data.jpa.domain.Specification;

import com.spotit.backend.domain.employee.employeeDetails.experience.Experience;
import com.spotit.backend.domain.referenceData.foreignLanguageName.ForeignLanguageName;
import com.spotit.backend.domain.referenceData.techSkillName.TechSkillName;
import com.spotit.backend.domain.referenceData.workMode.WorkMode;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JobOfferSearchCriteria implements Supplier<Specification<JobOffer>> {

    private static final String TECH_SKILL_NAMES_FIELD = "techSkillNames";
    private static final String FOREIGN_LANGUAGE_NAMES_FIELD = "foreignLanguageNames";
    private static final String WORK_EXPERIENCE_FIELD = "workExperience";
    private static final String WORK_MODES_FIELD = "workModes";

    private List<Integer> techSkillNameIds;
    private List<Integer> foreignLanguageNameIds;
    private List<Integer> experienceNameIds;
    private List<Integer> workModeIds;
    private LocalDate minDueDate;

    @Override
    public Specification<JobOffer> get() {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            addTechSkillNamesPredicate(root, predicates);
            addForeignLanguageNamesPredicate(root, predicates);
            addExperienceNamePredicate(root, predicates);
            addWorkModePredicate(root, predicates);
            addDueDatePredicate(root, builder, predicates);

            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private void addTechSkillNamesPredicate(Root<JobOffer> root, List<Predicate> predicates) {
        if (isNotEmpty(techSkillNameIds)) {
            Join<JobOffer, TechSkillName> techSkillNameJoin = root.join(TECH_SKILL_NAMES_FIELD);
            predicates.add(techSkillNameJoin.get("id").in(techSkillNameIds));
        }
    }

    private void addForeignLanguageNamesPredicate(Root<JobOffer> root, List<Predicate> predicates) {
        if (isNotEmpty(foreignLanguageNameIds)) {
            Join<JobOffer, ForeignLanguageName> foreignLanguageNameJoin = root.join(FOREIGN_LANGUAGE_NAMES_FIELD);
            predicates.add(foreignLanguageNameJoin.get("id").in(foreignLanguageNameIds));
        }
    }

    private void addExperienceNamePredicate(Root<JobOffer> root, List<Predicate> predicates) {
        if (isNotEmpty(experienceNameIds)) {
            Join<JobOffer, Experience> experienceJoin = root.join(WORK_EXPERIENCE_FIELD);
            predicates.add(experienceJoin.get("id").in(experienceNameIds));
        }
    }

    private void addWorkModePredicate(Root<JobOffer> root, List<Predicate> predicates) {
        if (isNotEmpty(workModeIds)) {
            Join<JobOffer, WorkMode> workModeJoin = root.join(WORK_MODES_FIELD);
            predicates.add(workModeJoin.get("id").in(workModeIds));
        }
    }

    private void addDueDatePredicate(Root<JobOffer> root, CriteriaBuilder builder, List<Predicate> predicates) {
        if (minDueDate != null) {
            predicates.add(builder.greaterThan(root.get("dueDate"), minDueDate));
        }
    }

    public void setMinDueDate(LocalDate minDueDate) {
        this.minDueDate = minDueDate;
    }

    private boolean isNotEmpty(List<?> list) {
        return list != null && !list.isEmpty();
    }
}
