package com.spotit.backend.abstraction.service;

import java.io.Serializable;

import com.spotit.backend.abstraction.model.AbstractEntity;

public interface AbstractService<E extends AbstractEntity, ID extends Serializable> {

    E getById(ID id);

    E create(E entity);

    void delete(ID id);

    E update(ID id, E entity);
}
