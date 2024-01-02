package com.spotit.backend.employee.portfolio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Integer> {

    Optional<Portfolio> findByPortfolioUrl(String portfolioUrl);

    Optional<Portfolio> findByUserAccountAuth0Id(String auth0Id);
}
