package com.spotit.backend.employee.abstraction;

public interface EntityMapper<E extends AbstractEntity, R extends ReadDto, W extends WriteDto> {

    R toReadDto(E entity);

    E fromWriteDto(W writeDto);
}
