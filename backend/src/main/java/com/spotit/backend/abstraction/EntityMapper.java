package com.spotit.backend.abstraction;

public interface EntityMapper<E extends AbstractEntity, R extends ReadDto, W extends WriteDto> {

    R toReadDto(E entity);

    E fromWriteDto(W writeDto);
}
