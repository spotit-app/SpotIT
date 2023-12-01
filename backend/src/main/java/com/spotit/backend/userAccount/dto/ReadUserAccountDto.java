package com.spotit.backend.userAccount.dto;

import java.util.List;

import com.spotit.backend.abstraction.dto.ReadDto;
import com.spotit.backend.course.dto.CourseDto;
import com.spotit.backend.education.dto.EducationDto;
import com.spotit.backend.experience.dto.ExperienceDto;
import com.spotit.backend.foreignLanguage.dto.ReadForeignLanguageDto;
import com.spotit.backend.interest.dto.ReadInterestDto;
import com.spotit.backend.project.dto.ReadProjectDto;
import com.spotit.backend.social.dto.ReadSocialDto;
import com.spotit.backend.softSkill.dto.ReadSoftSkillDto;
import com.spotit.backend.techSkill.dto.ReadTechSkillDto;

import lombok.Builder;

@Builder
public record ReadUserAccountDto(
        Integer id,
        String auth0Id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String profilePictureUrl,
        String position,
        String description,
        String cvClause,
        List<ReadSocialDto> socials,
        List<EducationDto> educations,
        List<ExperienceDto> experiences,
        List<ReadTechSkillDto> techSkills,
        List<ReadSoftSkillDto> softSkills,
        List<ReadForeignLanguageDto> foreignLanguages,
        List<ReadInterestDto> interests,
        List<CourseDto> courses,
        List<ReadProjectDto> projects) implements ReadDto {
}
