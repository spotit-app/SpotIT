package com.spotit.backend.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.mapper.EntityMapper;
import com.spotit.backend.project.dto.ReadProjectDto;
import com.spotit.backend.project.dto.WriteProjectDto;
import com.spotit.backend.project.model.Project;

@Mapper
public interface ProjectMapper extends EntityMapper<Project, ReadProjectDto, WriteProjectDto> {

    @Override
    ReadProjectDto toReadDto(Project project);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userAccount", ignore = true)
    Project fromWriteDto(WriteProjectDto writeProjectDto);
}
