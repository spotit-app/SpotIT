package com.spotit.backend.domain.jobApplication;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.EntityMapper;
import com.spotit.backend.domain.employee.portfolio.PortfolioMapper;
import com.spotit.backend.domain.employer.jobOffer.JobOfferMapper;

@Mapper(uses = {
        JobOfferMapper.class,
        PortfolioMapper.class
})
public interface JobApplicationMapper
        extends EntityMapper<JobApplication, JobApplicationReadDto, JobApplicationWriteDto> {

    @Override
    @Mapping(target = "applicationStatus", expression = "java(jobApplication.getApplicationStatus().getName())")
    JobApplicationReadDto toReadDto(JobApplication jobApplication);

    @Mapping(target = "applicationStatus", expression = "java(jobApplication.getApplicationStatus().getName())")
    JobApplicationUserReadDto toUserReadDto(JobApplication jobApplication);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "jobOffer", ignore = true)
    @Mapping(target = "portfolio", ignore = true)
    @Mapping(target = "applicationStatus", ignore = true)
    JobApplication fromWriteDto(JobApplicationWriteDto jobApplicationWriteDto);
}
