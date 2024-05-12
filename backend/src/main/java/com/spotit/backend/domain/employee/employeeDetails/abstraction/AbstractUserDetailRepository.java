package com.spotit.backend.domain.employee.employeeDetails.abstraction;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractUserDetailRepository<E extends AbstractUserDetailEntity, ID extends Serializable>
        extends JpaRepository<E, ID> {

    List<E> findByUserAccountAuth0Id(String userAccountAuth0Id);
}
