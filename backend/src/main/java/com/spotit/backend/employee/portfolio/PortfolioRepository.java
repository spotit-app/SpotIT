package com.spotit.backend.employee.portfolio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PortfolioRepository extends JpaRepository<Portfolio, Integer>, JpaSpecificationExecutor<Portfolio> {

    Optional<Portfolio> findByPortfolioUrl(String portfolioUrl);

    Optional<Portfolio> findByUserAccountAuth0Id(String auth0Id);
}
