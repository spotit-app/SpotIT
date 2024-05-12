package com.spotit.backend.domain.referenceData.abstraction;

import java.io.Serializable;
import java.util.List;

import com.spotit.backend.abstraction.AbstractEntity;

public interface AbstractReferenceDataService<E extends AbstractEntity, ID extends Serializable> {

    List<E> getAll();

    E getById(ID id);

    E create(E entity);

    E update(ID id, E entity);

    void delete(ID id);
}
