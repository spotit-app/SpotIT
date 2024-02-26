package com.spotit.backend.employee.portfolio;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.employee.userAccount.UserAccountMapper;
import com.spotit.backend.employee.userDetails.course.CourseMapper;
import com.spotit.backend.employee.userDetails.education.EducationMapper;
import com.spotit.backend.employee.userDetails.experience.ExperienceMapper;
import com.spotit.backend.employee.userDetails.foreignLanguage.ForeignLanguageMapper;
import com.spotit.backend.employee.userDetails.interest.InterestMapper;
import com.spotit.backend.employee.userDetails.project.ProjectMapper;
import com.spotit.backend.employee.userDetails.social.SocialMapper;
import com.spotit.backend.employee.userDetails.softSkill.SoftSkillMapper;
import com.spotit.backend.employee.userDetails.techSkill.TechSkillMapper;

@Mapper(uses = {
        UserAccountMapper.class,
        CourseMapper.class,
        EducationMapper.class,
        ExperienceMapper.class,
        ForeignLanguageMapper.class,
        InterestMapper.class,
        ProjectMapper.class,
        SocialMapper.class,
        SoftSkillMapper.class,
        TechSkillMapper.class
})
public interface PortfolioMapper {

    @Mapping(target = "userData", source = "userAccount")
    PortfolioReadDto toReadDto(Portfolio portfolio);

    @Mapping(target = "userData", source = "userAccount")
    PortfolioListReadDto toListReadDto(Portfolio portfolio);
}

