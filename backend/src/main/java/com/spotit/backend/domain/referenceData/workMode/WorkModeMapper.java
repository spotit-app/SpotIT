package com.spotit.backend.domain.referenceData.workMode;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.EntityMapper;

@Mapper
public interface WorkModeMapper
        extends EntityMapper<WorkMode, WorkModeReadDto, WorkModeWriteDto> {

    @Override
    WorkModeReadDto toReadDto(WorkMode workMode);

    @Override
    @Mapping(target = "id", ignore = true)
    WorkMode fromWriteDto(WorkModeWriteDto writeDto);
}
