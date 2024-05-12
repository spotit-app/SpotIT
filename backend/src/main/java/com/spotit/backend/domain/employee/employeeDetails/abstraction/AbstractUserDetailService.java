package com.spotit.backend.domain.employee.employeeDetails.abstraction;

import java.io.Serializable;
import java.util.List;

import com.spotit.backend.abstraction.AbstractEntity;

public interface AbstractUserDetailService<E extends AbstractEntity, ID extends Serializable> {

    List<E> getAllByUserAccountAuth0Id(String userAccountAuth0Id);

    E getById(ID id);

    E create(String userAccountAuth0Id, E entity);

    E update(ID id, E entity);

    void delete(ID id);
}
