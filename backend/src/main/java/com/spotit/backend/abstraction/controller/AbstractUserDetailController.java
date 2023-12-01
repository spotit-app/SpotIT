package com.spotit.backend.abstraction.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.spotit.backend.abstraction.dto.ReadDto;
import com.spotit.backend.abstraction.dto.WriteDto;
import com.spotit.backend.abstraction.mapper.EntityMapper;
import com.spotit.backend.abstraction.model.AbstractEntity;
import com.spotit.backend.abstraction.service.AbstractUserDetailService;

public abstract class AbstractUserDetailController<E extends AbstractEntity, ID extends Serializable, R extends ReadDto, W extends WriteDto>
        extends AbstractController<E, ID, R, W> {

    protected AbstractUserDetailService<E, ID> service;

    public AbstractUserDetailController(
            AbstractUserDetailService<E, ID> service,
            EntityMapper<E, R, W> mapper) {
        super(service, mapper);
        this.service = service;
    }

    @GetMapping
    public List<R> getAllEntitiesOfUser(@PathVariable String auth0Id) {
        return service.getAllByUserAccountAuth0Id(auth0Id)
                .stream()
                .map(mapper::toReadDto)
                .toList();
    }

    @PutMapping("/{id}")
    public R updateEntityById(@PathVariable ID id, @RequestBody W writeDto) {
        E entityToUpdate = mapper.fromWriteDto(writeDto);
        E editedEntity = service.update(id, entityToUpdate);

        return mapper.toReadDto(editedEntity);
    }

    @PostMapping
    public R createEntityOfUser(
            @PathVariable String auth0Id,
            @RequestBody W writeDto) {
        E entityToCreate = mapper.fromWriteDto(writeDto);
        E createdEntity = service.create(auth0Id, entityToCreate);

        return mapper.toReadDto(createdEntity);
    }
}
