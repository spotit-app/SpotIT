package com.spotit.backend.employee.userDetails.project;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.employee.abstraction.EntityMapper;

@Mapper
public interface ProjectMapper extends EntityMapper<Project, ProjectReadDto, ProjectWriteDto> {

    @Override
    ProjectReadDto toReadDto(Project project);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userAccount", ignore = true)
    Project fromWriteDto(ProjectWriteDto writeProjectDto);
}
