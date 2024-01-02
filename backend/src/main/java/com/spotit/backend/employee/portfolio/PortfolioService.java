package com.spotit.backend.employee.portfolio;

public interface PortfolioService {

    Portfolio getByUserAccountAuth0Id(String auth0Id);

    Portfolio getByUrl(String portfolioUrl);

    Portfolio create(String auth0Id);

    Portfolio update(String auth0Id);
}
