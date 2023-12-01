package com.spotit.backend.userAccount.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.mapper.EntityMapper;
import com.spotit.backend.course.mapper.CourseMapper;
import com.spotit.backend.education.mapper.EducationMapper;
import com.spotit.backend.experience.mapper.ExperienceMapper;
import com.spotit.backend.foreignLanguage.mapper.ForeignLanguageMapper;
import com.spotit.backend.interest.mapper.InterestMapper;
import com.spotit.backend.project.mapper.ProjectMapper;
import com.spotit.backend.social.mapper.SocialMapper;
import com.spotit.backend.softSkill.mapper.SoftSkillMapper;
import com.spotit.backend.techSkill.mapper.TechSkillMapper;
import com.spotit.backend.userAccount.dto.ReadUserAccountDto;
import com.spotit.backend.userAccount.dto.WriteUserAccountDto;
import com.spotit.backend.userAccount.model.UserAccount;

@Mapper(uses = {
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
public interface UserAccountMapper extends EntityMapper<UserAccount, ReadUserAccountDto, WriteUserAccountDto> {

    @Override
    ReadUserAccountDto toReadDto(UserAccount userAccount);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "socials", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "interests", ignore = true)
    @Mapping(target = "educations", ignore = true)
    @Mapping(target = "softSkills", ignore = true)
    @Mapping(target = "techSkills", ignore = true)
    @Mapping(target = "experiences", ignore = true)
    @Mapping(target = "foreignLanguages", ignore = true)
    @Mapping(target = "profilePictureUrl", ignore = true)
    UserAccount fromWriteDto(WriteUserAccountDto writeUserAccountDto);
}
