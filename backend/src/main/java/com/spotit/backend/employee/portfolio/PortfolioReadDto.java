package com.spotit.backend.employee.portfolio;

import java.util.List;

import com.spotit.backend.employee.abstraction.ReadDto;
import com.spotit.backend.employee.userAccount.UserAccountReadDataDto;
import com.spotit.backend.employee.userDetails.course.CourseReadDto;
import com.spotit.backend.employee.userDetails.education.EducationReadDto;
import com.spotit.backend.employee.userDetails.experience.ExperienceReadDto;
import com.spotit.backend.employee.userDetails.foreignLanguage.ForeignLanguageReadDto;
import com.spotit.backend.employee.userDetails.interest.InterestReadDto;
import com.spotit.backend.employee.userDetails.project.ProjectReadDto;
import com.spotit.backend.employee.userDetails.social.SocialReadDto;
import com.spotit.backend.employee.userDetails.softSkill.SoftSkillReadDto;
import com.spotit.backend.employee.userDetails.techSkill.TechSkillReadDto;

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
