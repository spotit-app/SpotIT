package com.spotit.backend.domain.employee.portfolio;

public interface PortfolioUtils {

    static Portfolio generatePortfolio(Integer id) {
        return Portfolio.builder()
                .portfolioUrl("portfolio-url" + id)
                .build();
    }

    static String getInvalidUserMessage() {
        return "User account lacks necessary data!";
    }
}
