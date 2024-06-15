package com.spotit.backend.domain.employee.portfolio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class PortfolioController {

    private final PortfolioMapper portfolioMapper;
    private final PortfolioService portfolioService;

    public PortfolioController(
            PortfolioMapper portfolioMapper,
            PortfolioService portfolioService) {
        this.portfolioMapper = portfolioMapper;
        this.portfolioService = portfolioService;
    }

    @GetMapping("/portfolio/{portfolioUrl}")
    public PortfolioReadDto getPortfolioByUrl(@PathVariable String portfolioUrl) {
        return portfolioMapper.toReadDto(portfolioService.getByUrl(portfolioUrl));
    }

    @GetMapping("/portfolios")
    public Page<PortfolioListReadDto> getPortfolios(
            @PageableDefault(size = 5) Pageable pageable,
            PortfolioSearchCriteria portfolioSearchCriteria) {

        return portfolioService.findByCriteria(portfolioSearchCriteria, pageable)
                .map(portfolioMapper::toListReadDto);
    }

    @GetMapping("/userAccount/{auth0Id}/portfolio")
    public String getPortfolioByUserAccountAuth0Id(@PathVariable String auth0Id) {
        return portfolioService.getByUserAccountAuth0Id(auth0Id).getPortfolioUrl();
    }

    @PostMapping("/userAccount/{auth0Id}/portfolio")
    public PortfolioReadDto createPortfolio(@PathVariable String auth0Id) {
        Portfolio createdPortfolio = portfolioService.create(auth0Id);

        return portfolioMapper.toReadDto(createdPortfolio);
    }

    @PutMapping("/userAccount/{auth0Id}/portfolio")
    public PortfolioReadDto updatePortfolio(@PathVariable String auth0Id) {
        Portfolio updatedPortfolio = portfolioService.update(auth0Id);

        return portfolioMapper.toReadDto(updatedPortfolio);
    }
}
