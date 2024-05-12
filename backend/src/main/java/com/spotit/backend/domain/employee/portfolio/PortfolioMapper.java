package com.spotit.backend.domain.employee.portfolio;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.domain.employee.employeeDetails.course.CourseMapper;
import com.spotit.backend.domain.employee.employeeDetails.education.EducationMapper;
import com.spotit.backend.domain.employee.employeeDetails.experience.ExperienceMapper;
import com.spotit.backend.domain.employee.employeeDetails.foreignLanguage.ForeignLanguageMapper;
import com.spotit.backend.domain.employee.employeeDetails.interest.InterestMapper;
import com.spotit.backend.domain.employee.employeeDetails.project.ProjectMapper;
import com.spotit.backend.domain.employee.employeeDetails.social.SocialMapper;
import com.spotit.backend.domain.employee.employeeDetails.softSkill.SoftSkillMapper;
import com.spotit.backend.domain.employee.employeeDetails.techSkill.TechSkillMapper;
import com.spotit.backend.domain.userAccount.UserAccountMapper;

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
