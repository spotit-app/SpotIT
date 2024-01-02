package com.spotit.backend.employee.userDetails.abstraction;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.spotit.backend.employee.abstraction.AbstractEntity;
import com.spotit.backend.employee.abstraction.EntityMapper;
import com.spotit.backend.employee.abstraction.ReadDto;
import com.spotit.backend.employee.abstraction.WriteDto;

public abstract class AbstractUserDetailController<E extends AbstractEntity, ID extends Serializable, R extends ReadDto, W extends WriteDto> {

    protected AbstractUserDetailService<E, ID> service;
    protected EntityMapper<E, R, W> mapper;

    public AbstractUserDetailController(
            AbstractUserDetailService<E, ID> service,
            EntityMapper<E, R, W> mapper) {
        this.mapper = mapper;
        this.service = service;
    }

    @GetMapping
    public List<R> getAllEntitiesOfUser(@PathVariable String auth0Id) {
        return service.getAllByUserAccountAuth0Id(auth0Id)
                .stream()
                .map(mapper::toReadDto)
                .toList();
    }

    @GetMapping("/{id}")
    public R getEntityById(@PathVariable ID id) {
        return mapper.toReadDto(service.getById(id));
    }

    @PostMapping
    public R createEntityOfUser(
            @PathVariable String auth0Id,
            @RequestBody W writeDto) {
        E entityToCreate = mapper.fromWriteDto(writeDto);
        E createdEntity = service.create(auth0Id, entityToCreate);

        return mapper.toReadDto(createdEntity);
    }

    @PutMapping("/{id}")
    public R updateEntityById(@PathVariable ID id, @RequestBody W writeDto) {
        E entityToUpdate = mapper.fromWriteDto(writeDto);
        E editedEntity = service.update(id, entityToUpdate);

        return mapper.toReadDto(editedEntity);
    }

    @DeleteMapping("/{id}")
    public Map<String, ID> deleteById(@PathVariable ID id) {
        service.delete(id);

        return Map.of("id", id);
    }
}
