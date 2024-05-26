package com.spotit.backend.domain.employer.jobOffer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.EntityMapper;
import com.spotit.backend.domain.employer.company.CompanyMapper;
import com.spotit.backend.domain.referenceData.foreignLanguageName.ForeignLanguageNameMapper;
import com.spotit.backend.domain.referenceData.softSkillName.SoftSkillNameMapper;
import com.spotit.backend.domain.referenceData.techSkillName.TechSkillNameMapper;
import com.spotit.backend.domain.referenceData.workMode.WorkModeMapper;

@Mapper(uses = {
        CompanyMapper.class,
        TechSkillNameMapper.class,
        SoftSkillNameMapper.class,
        ForeignLanguageNameMapper.class,
        WorkModeMapper.class,
})
public interface JobOfferMapper extends EntityMapper<JobOffer, JobOfferReadDto, JobOfferWriteDto> {

    @Override
    @Mapping(target = "workExperienceName", expression = "java(jobOffer.getWorkExperience().getName())")
    JobOfferReadDto toReadDto(JobOffer jobOffer);

    @Mapping(target = "workExperienceName", expression = "java(jobOffer.getWorkExperience().getName())")
    JobOfferListReadDto toListReadDto(JobOffer jobOffer);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "workExperience", ignore = true)
    @Mapping(target = "techSkillNames", ignore = true)
    @Mapping(target = "softSkillNames", ignore = true)
    @Mapping(target = "foreignLanguageNames", ignore = true)
    @Mapping(target = "workModes", ignore = true)
    JobOffer fromWriteDto(JobOfferWriteDto jobOfferWriteDto);
}
