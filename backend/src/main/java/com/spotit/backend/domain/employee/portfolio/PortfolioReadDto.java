package com.spotit.backend.domain.employee.portfolio;

import java.util.List;

import com.spotit.backend.abstraction.ReadDto;
import com.spotit.backend.domain.employee.employeeDetails.course.CourseReadDto;
import com.spotit.backend.domain.employee.employeeDetails.education.EducationReadDto;
import com.spotit.backend.domain.employee.employeeDetails.experience.ExperienceReadDto;
import com.spotit.backend.domain.employee.employeeDetails.foreignLanguage.ForeignLanguageReadDto;
import com.spotit.backend.domain.employee.employeeDetails.interest.InterestReadDto;
import com.spotit.backend.domain.employee.employeeDetails.project.ProjectReadDto;
import com.spotit.backend.domain.employee.employeeDetails.social.SocialReadDto;
import com.spotit.backend.domain.employee.employeeDetails.softSkill.SoftSkillReadDto;
import com.spotit.backend.domain.employee.employeeDetails.techSkill.TechSkillReadDto;
import com.spotit.backend.domain.userAccount.UserAccountReadDataDto;

import lombok.Builder;

@Builder
public record PortfolioReadDto(
        String portfolioUrl,
        UserAccountReadDataDto userData,
        List<CourseReadDto> courses,
        List<EducationReadDto> educations,
        List<ExperienceReadDto> experiences,
        List<ForeignLanguageReadDto> foreignLanguages,
        List<InterestReadDto> interests,
        List<ProjectReadDto> projects,
        List<SocialReadDto> socials,
        List<SoftSkillReadDto> softSkills,
        List<TechSkillReadDto> techSkills) implements ReadDto {
}
