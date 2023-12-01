package com.spotit.backend.abstraction.controller;

import java.io.Serializable;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.spotit.backend.abstraction.dto.ReadDto;
import com.spotit.backend.abstraction.dto.WriteDto;
import com.spotit.backend.abstraction.mapper.EntityMapper;
import com.spotit.backend.abstraction.model.AbstractEntity;
import com.spotit.backend.abstraction.service.AbstractService;

public abstract class AbstractController<E extends AbstractEntity, ID extends Serializable, R extends ReadDto, W extends WriteDto> {

    protected final AbstractService<E, ID> service;
    protected final EntityMapper<E, R, W> mapper;

    public AbstractController(AbstractService<E, ID> service, EntityMapper<E, R, W> mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public R getEntityById(@PathVariable ID id) {
        return mapper.toReadDto(service.getById(id));
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable ID id) {
        service.delete(id);

        return "Entity with ID '" + id + "' deleted.";
    }
}
