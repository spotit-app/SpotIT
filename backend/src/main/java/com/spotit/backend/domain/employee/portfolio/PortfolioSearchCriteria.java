package com.spotit.backend.domain.employee.portfolio;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.data.jpa.domain.Specification;

import com.spotit.backend.domain.employee.employeeDetails.foreignLanguage.ForeignLanguage;
import com.spotit.backend.domain.employee.employeeDetails.techSkill.TechSkill;
import com.spotit.backend.domain.referenceData.foreignLanguageName.ForeignLanguageName;
import com.spotit.backend.domain.referenceData.techSkillName.TechSkillName;
import com.spotit.backend.domain.userAccount.UserAccount;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PortfolioSearchCriteria implements Supplier<Specification<Portfolio>> {

    private static final String FOREIGN_LANGUAGE_NAMES_FIELD = "foreignLanguageName";
    private static final String FOREIGN_LANGUAGES_FIELD = "foreignLanguages";
    private static final String TECH_SKILL_NAMES_FIELD = "techSkillName";
    private static final String USER_ACCOUNT_FIELD = "userAccount";
    private static final String TECH_SKILLS_FIELD = "techSkills";

    private List<Integer> techSkillNameIds;
    private List<Integer> foreignLanguageNameIds;

    @Override
    public Specification<Portfolio> get() {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            addIsOpenPredicate(root, builder, predicates);

            addTechSkillNamesPredicate(root, predicates);

            addForeignLanguageNamesPredicate(root, predicates);

            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private void addIsOpenPredicate(Root<Portfolio> root, CriteriaBuilder builder, List<Predicate> predicates) {
        Join<UserAccount, Portfolio> portfolio_userAccount = root.join(USER_ACCOUNT_FIELD);

        predicates.add(builder.isTrue(portfolio_userAccount.get("isOpen")));
    }

    private void addTechSkillNamesPredicate(Root<Portfolio> root, List<Predicate> predicates) {
        if (isNotEmpty(techSkillNameIds)) {
            Join<TechSkill, Portfolio> portfolio_techSkill = root.join(TECH_SKILLS_FIELD);
            Join<TechSkillName, TechSkill> techSkill_techSkillName = portfolio_techSkill
                    .join(TECH_SKILL_NAMES_FIELD);

            predicates.add(techSkill_techSkillName.get("id").in(techSkillNameIds));
        }
    }

    private void addForeignLanguageNamesPredicate(Root<Portfolio> root, List<Predicate> predicates) {
        if (isNotEmpty(foreignLanguageNameIds)) {
            Join<ForeignLanguage, Portfolio> portfolio_foreignLanguage = root.join(FOREIGN_LANGUAGES_FIELD);
            Join<ForeignLanguageName, ForeignLanguage> foreignLanguage_foreignLanguageName = portfolio_foreignLanguage
                    .join(FOREIGN_LANGUAGE_NAMES_FIELD);

            predicates.add(foreignLanguage_foreignLanguageName.get("id").in(foreignLanguageNameIds));
        }
    }

    private boolean isNotEmpty(List<?> list) {
        return list != null && !list.isEmpty();
    }
}
