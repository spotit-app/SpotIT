package com.spotit.backend.abstraction.mapper;

import com.spotit.backend.abstraction.dto.ReadDto;
import com.spotit.backend.abstraction.dto.WriteDto;
import com.spotit.backend.abstraction.model.AbstractEntity;

public interface EntityMapper<E extends AbstractEntity, R extends ReadDto, W extends WriteDto> {

    R toReadDto(E entity);

    E fromWriteDto(W writeDto);
}
