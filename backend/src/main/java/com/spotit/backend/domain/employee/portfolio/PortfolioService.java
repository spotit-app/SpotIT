package com.spotit.backend.domain.employee.portfolio;

import java.util.function.Supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface PortfolioService {

    Portfolio getByUserAccountAuth0Id(String auth0Id);

    Portfolio getByUrl(String portfolioUrl);

    Portfolio create(String auth0Id);

    Portfolio update(String auth0Id);

    Page<Portfolio> findByCriteria(Supplier<Specification<Portfolio>> portfolioSearchCriteria, Pageable pageable);
}
