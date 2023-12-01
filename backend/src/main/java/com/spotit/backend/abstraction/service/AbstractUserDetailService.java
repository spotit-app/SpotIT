package com.spotit.backend.abstraction.service;

import java.io.Serializable;
import java.util.List;

import com.spotit.backend.abstraction.model.AbstractEntity;

public interface AbstractUserDetailService<E extends AbstractEntity, ID extends Serializable>
        extends AbstractService<E, ID> {

    List<E> getAllByUserAccountAuth0Id(String userAccountAuth0Id);

    E create(String userAccountAuth0Id, E entity);
}
