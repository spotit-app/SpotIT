package com.spotit.backend.employee.portfolio;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.spotit.backend.employee.abstraction.EntityNotFoundException;
import com.spotit.backend.employee.abstraction.ErrorCreatingEntityException;
import com.spotit.backend.employee.userAccount.UserAccount;
import com.spotit.backend.employee.userAccount.UserAccountService;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserAccountService userAccountService;

    public PortfolioServiceImpl(
            PortfolioRepository portfolioRepository,
            UserAccountService userAccountService) {
        this.portfolioRepository = portfolioRepository;
        this.userAccountService = userAccountService;
    }

    @Override
    @Cacheable(key = "#portfolioUrl")
    public Portfolio getByUrl(String portfolioUrl) {
        return portfolioRepository.findByPortfolioUrl(portfolioUrl)
                .orElseThrow(() -> new EntityNotFoundException(portfolioUrl));
    }

    @Override
    @Cacheable(key = "#auth0Id")
    public Portfolio getByUserAccountAuth0Id(String auth0Id) {
        return portfolioRepository.findByUserAccountAuth0Id(auth0Id)
                .orElseThrow(() -> new EntityNotFoundException(auth0Id));
    }

    @Override
    @CachePut(key = "#result.portfolioUrl")
    public Portfolio create(String auth0Id) {
        UserAccount userAccount = userAccountService.getByAuth0Id(auth0Id);

        if (isUserInValid(userAccount)) {
            throw new InvalidUserException();
        }

        String portoflioUrl = getPortfolioUrl(userAccount);

        Portfolio portfolio = Portfolio.builder().portfolioUrl(portoflioUrl).userAccount(userAccount).build();

        return create(portfolio);
    }

    public Portfolio create(Portfolio portfolio) {
        try {
            return portfolioRepository.save(portfolio);
        } catch (Exception e) {
            throw new ErrorCreatingEntityException();
        }
    }

    @Override
    @CachePut(key = "#result.portfolioUrl")
    public Portfolio update(String auth0Id) {
        UserAccount userAccount = userAccountService.getByAuth0Id(auth0Id);

        if (isUserInValid(userAccount)) {
            throw new InvalidUserException();
        }

        String newPortfolioUrl = getPortfolioUrl(userAccount);

        Portfolio portfolio = getByUserAccountAuth0Id(auth0Id);
        portfolio.setPortfolioUrl(newPortfolioUrl);

        return create(portfolio);
    }

    private boolean isUserInValid(UserAccount userAccount) {
        return userAccount.getFirstName() == null || userAccount.getLastName() == null;
    }

    private String getPortfolioUrl(UserAccount userAccount) {
        return new StringBuilder()
                .append(userAccount.getFirstName())
                .append("_")
                .append(userAccount.getLastName())
                .append(userAccount.getId())
                .toString();
    }
}
